#!/usr/bin/env python3
"""Паритет ключей между локалями.

Курс требует «строк в коде нет» с первой вехи. Требование проверяемо только вместе
с паритетом: ключ, забытый в одной локали, при переключении языка молча оставит
на экране чужой язык — и глазами это ловится через раз.

Раскладку проекта скрипт не знает и не должен: каталоги composeResources он ищет
сам. Поэтому он одинаково работает и в шаблоне воркшопа, и в вашем проекте из
визарда, как бы ни назывался модуль.

Запуск:  python3 tools/check-strings.py
Код возврата 1, если локали разошлись или сверять нечего.
"""
import re
import sys
from pathlib import Path

BASE = "values"
MARKERS = ("settings.gradle.kts", "settings.gradle", ".git")
SKIP = {"build", ".git", ".gradle", ".kotlin", ".idea", "node_modules", "DerivedData"}


def project_root() -> Path:
    """Корень проекта — ближайший каталог со сборочным маркером."""
    for start in (Path.cwd(), Path(__file__).resolve().parent):
        for candidate in (start, *start.parents):
            if any((candidate / m).exists() for m in MARKERS):
                return candidate
    return Path.cwd()


def resource_dirs(root: Path) -> list[Path]:
    """Все composeResources проекта, в любом модуле и на любой глубине."""
    found = []
    for path in root.rglob("composeResources"):
        if not path.is_dir():
            continue
        if SKIP & set(path.relative_to(root).parts):
            continue
        found.append(path)
    return sorted(found)


def keys(values_dir: Path) -> set[str]:
    """Ключи всех строковых файлов локали: их может быть больше одного."""
    result: set[str] = set()
    for xml in sorted(values_dir.glob("*.xml")):
        result |= set(re.findall(r'name="([^"]+)"', xml.read_text(encoding="utf-8")))
    return result


def check(res: Path, root: Path) -> tuple[bool, bool]:
    """Возвращает (сверяли ли что-нибудь, есть ли расхождения)."""
    base_dir = res / BASE
    others = sorted(p for p in res.glob("values-*") if p.is_dir() and any(p.glob("*.xml")))
    if not base_dir.is_dir() or not any(base_dir.glob("*.xml")):
        if others:
            print(f"{res.relative_to(root)}: есть локали, но нет базовой {BASE}/")
            return True, True
        return False, False
    if not others:
        print(f"{res.relative_to(root)}: только базовая локаль — сверять не с чем")
        return False, False

    print(f"{res.relative_to(root)}")
    base = keys(base_dir)
    failed = False
    for path in others:
        locale = path.name
        other = keys(path)
        missing = sorted(base - other)
        extra = sorted(other - base)
        if missing or extra:
            failed = True
            print(f"  {locale}: расхождений {len(missing) + len(extra)}")
            for k in missing:
                print(f"    нет в {locale}: {k}")
            for k in extra:
                print(f"    нет в {BASE}: {k}")
        else:
            print(f"  {locale}: 0 расхождений, ключей {len(other)}")
    return True, failed


def main() -> int:
    root = project_root()
    dirs = resource_dirs(root)
    if not dirs:
        print(f"не нашёл ни одного каталога composeResources под {root}")
        print("подписи интерфейса должны лежать в ресурсах — это требование вехи В1")
        return 1

    checked_any = False
    failed = False
    for res in dirs:
        checked, bad = check(res, root)
        checked_any |= checked
        failed |= bad

    if not checked_any:
        print("нет ни одной пары локалей — нечего сверять")
        return 1
    return 1 if failed else 0


if __name__ == "__main__":
    sys.exit(main())

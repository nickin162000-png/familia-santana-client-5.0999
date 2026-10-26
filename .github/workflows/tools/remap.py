#!/usr/bin/env python3
"""Converte os nomes internos do Minecraft (func_XXXX / field_XXXX) para os nomes
legiveis do MCP, usando a tabela oficial que o ForgeGradle acabou de baixar."""
import csv
import io
import os
import re
import sys
import zipfile

HOME = os.path.expanduser("~")
GRADLE = os.path.join(HOME, ".gradle")
SRC = os.path.join(os.getcwd(), "src", "main", "java")

mapping = {}


def add(text):
    for row in csv.reader(io.StringIO(text)):
        if len(row) >= 2 and (row[0].startswith("func_") or row[0].startswith("field_")):
            mapping[row[0]] = row[1]


# 1) tabela dentro do zip mcp_stable baixado pelo Gradle
for dp, dn, fn in os.walk(GRADLE):
    for f in fn:
        path = os.path.join(dp, f)
        if f.startswith("mcp_stable") and f.endswith(".zip"):
            with zipfile.ZipFile(path) as zf:
                for n in zf.namelist():
                    if n.endswith("fields.csv") or n.endswith("methods.csv"):
                        add(zf.read(n).decode("utf-8", "replace"))
            print("Tabela lida de:", path)

# 2) alternativa: csv ja extraidos
if not mapping:
    for dp, dn, fn in os.walk(GRADLE):
        for f in fn:
            if f in ("fields.csv", "methods.csv"):
                with open(os.path.join(dp, f), encoding="utf-8", errors="replace") as fh:
                    add(fh.read())

if not mapping:
    print("ERRO: nao encontrei a tabela de nomes (mcp_stable) no cache do Gradle.")
    sys.exit(1)

print("Nomes na tabela:", len(mapping))

pat = re.compile(r"\b(?:func|field)_\d+_[A-Za-z0-9_]+\b")
missing = set()
total = 0


def sub(m):
    global total
    name = m.group(0)
    if name in mapping:
        total += 1
        return mapping[name]
    missing.add(name)
    return name


for dp, dn, fn in os.walk(SRC):
    for f in fn:
        if not f.endswith(".java"):
            continue
        path = os.path.join(dp, f)
        with open(path, encoding="utf-8") as fh:
            text = fh.read()
        new = pat.sub(sub, text)
        if new != text:
            with open(path, "w", encoding="utf-8") as fh:
                fh.write(new)

print("Trocas feitas:", total)
if missing:
    print("Sem nome na tabela (ficaram como estao):", ", ".join(sorted(missing)))

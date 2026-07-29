# pipeline-commandes-etudiant — Squelette de l'étude de cas

Squelette de projet pour l'étude de cas "pipeline de validation de commandes
clients". Reportez-vous à l'énoncé distribué pour le détail
des consignes.

## Lancer le projet

```bash
sbt compile   # compile le projet (échouera tant que les TODO ne sont pas résolus)
sbt run       # exécute le pipeline sur le jeu de données d'exemple
sbt test      # exécute les tests fournis pour vous auto-évaluer
```

## Travail attendu

Complétez les méthodes marquées `??? // TODO` dans
`src/main/scala/Pipeline.scala`, dans l'ordre proposé par les commentaires :

1. `parseIntOpt` / `parseDoubleOpt` — parsing sûr avec `Option`
2. `validerProduit` / `validerQuantite` / `validerPrix` — règles métier avec `Either`
3. `parseCommande` — assemblage du pipeline avec un `for`
4. `traiterLignes` — application à une liste de lignes (`map`)
5. `resumerResultat` — message lisible avec `fold`
6. `calculerStatistiques` — agrégation finale

Les tests de `src/test/scala/PipelineSpec.scala` doivent tous passer une fois
le travail terminé.

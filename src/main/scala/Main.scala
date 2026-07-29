package pipeline

/** Jeu de données d'exemple simulant un export CSV simplifié :
  * "id;produit;quantite;prixUnitaire"
  */
val lignesExemple: List[String] = List(
  "CMD001;Clavier;3;29.90",
  "CMD002;Souris;-1;9.90",
  "CMD003;;2;15.00",
  "CMD004;Ecran;1;149.99",
  "CMD005;Casque;deux;39.90",
  "CMD006;Cle USB;5;7.5"
)

@main def main(): Unit =
  val resultats = Pipeline.traiterLignes(lignesExemple)

  println("=== Traitement des commandes ===")
  resultats.map(Pipeline.resumerResultat).foreach(println)

  val stats = Pipeline.calculerStatistiques(resultats)
  println()
  println("=== Statistiques ===")
  println(s"Commandes valides   : ${stats.nbValides}")
  println(s"Commandes en erreur : ${stats.nbErreurs}")
  println(f"Montant total       : ${stats.montantTotal}%.2f EUR")

package pipeline

import org.scalatest.funsuite.AnyFunSuite

class PipelineSpec extends AnyFunSuite:

  // --- parsing (Option) ---------------------------------------------

  test("parseIntOpt retourne Some pour un entier valide") {
    assert(Pipeline.parseIntOpt("3") == Some(3))
  }

  test("parseIntOpt retourne None pour une valeur non numérique") {
    assert(Pipeline.parseIntOpt("deux") == None)
  }

  // --- validations (Either) ------------------------------------------

  test("validerQuantite accepte une quantité positive") {
    assert(Pipeline.validerQuantite("3") == Right(3))
  }

  test("validerQuantite refuse une quantité négative") {
    assert(Pipeline.validerQuantite("-1").isLeft)
  }

  test("validerQuantite refuse une valeur non numérique") {
    assert(Pipeline.validerQuantite("deux").isLeft)
  }

  test("validerProduit refuse un nom vide") {
    assert(Pipeline.validerProduit("").isLeft)
  }

  test("validerPrix refuse un prix nul ou négatif") {
    assert(Pipeline.validerPrix("0").isLeft)
  }

  // --- pipeline complet (for-comprehension) ---------------------------

  test("parseCommande construit une Commande valide") {
    val resultat = Pipeline.parseCommande("CMD001;Clavier;3;29.90")
    assert(resultat.isRight)
    resultat.foreach { c =>
      assert(c.id == "CMD001")
      assert(c.quantite == 3)
      assert(math.abs(c.montantTotal - 89.70) < 0.001)
    }
  }

  test("parseCommande renvoie une erreur explicite si la quantité est invalide") {
    val resultat = Pipeline.parseCommande("CMD002;Souris;-1;9.90")
    assert(resultat.isLeft)
  }

  test("parseCommande renvoie une erreur si le produit est vide") {
    val resultat = Pipeline.parseCommande("CMD003;;2;15.00")
    assert(resultat.isLeft)
  }

  // --- statistiques ----------------------------------------------------

  test("calculerStatistiques compte correctement succès et erreurs") {
    val resultats = Pipeline.traiterLignes(lignesExemple)
    val stats = Pipeline.calculerStatistiques(resultats)
    assert(stats.nbValides + stats.nbErreurs == lignesExemple.size)
    assert(stats.nbErreurs >= 2) // CMD002, CMD003 et CMD005 sont invalides
  }

end PipelineSpec

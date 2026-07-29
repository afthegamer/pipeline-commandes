package pipeline

/** Une commande client validée et prête à être exploitée. */
case class Commande(
    id: String,
    produit: String,
    quantite: Int,
    prixUnitaire: Double,
    montantTotal: Double
)

/** Statistiques agrégées sur un lot de lignes traitées. */
case class Statistiques(nbValides: Int, nbErreurs: Int, montantTotal: Double)

/** Pipeline de traitement de commandes clients — À COMPLÉTER.
  *
  * Chaque méthode ci-dessous contient un TODO. Reportez-vous à l'énoncé
  * fourni par le formateur pour le détail des règles à implémenter.
  */
object Pipeline:

  // ---------------------------------------------------------------------
  // Étape 1 — Parsing sûr des champs bruts avec Option
  // ---------------------------------------------------------------------

  /** Convertit une chaîne en Int, sans jamais lever d'exception.
    * Indice : la méthode `.toIntOption` existe déjà sur String.
    */
  def parseIntOpt(s: String): Option[Int] =
    s.toIntOption

  /** Convertit une chaîne en Double, sans jamais lever d'exception. */
  def parseDoubleOpt(s: String): Option[Double] =
    s.toDoubleOption

  // ---------------------------------------------------------------------
  // Étape 2 — Règles métier avec Either[String, A]
  // ---------------------------------------------------------------------

  /** Le produit ne doit pas être vide (après trim).
    * Right(produit) si valide, Left(message d'erreur) sinon.
    */
  def validerProduit(produitBrut: String): Either[String, String] =
    if produitBrut.trim.nonEmpty then Right(produitBrut.trim)
    else Left("Produit vide")

  /** La quantité doit être un entier strictement positif.
    * Utilisez parseIntOpt puis `.toRight(...)` pour obtenir un Either,
    * puis `.flatMap` pour appliquer la règle métier (> 0).
    */
  def validerQuantite(quantiteBrute: String): Either[String, Int] =
    parseIntOpt(quantiteBrute)
      .toRight(s"Quantité non numérique : '$quantiteBrute'")
      .flatMap: quantite =>
        if quantite > 0 then Right(quantite)
        else Left(s"Quantité invalide : $quantite (doit être > 0)")

  /** Le prix unitaire doit être un nombre strictement positif. */
  def validerPrix(prixBrut: String): Either[String, Double] =
    parseDoubleOpt(prixBrut)
      .toRight(s"Prix non numérique : '$prixBrut'")
      .flatMap: prix =>
        if prix > 0 then Right(prix)
        else Left(s"Prix invalide : $prix (doit être > 0)")

  // ---------------------------------------------------------------------
  // Étape 3 — Assemblage du pipeline avec for-comprehension
  // ---------------------------------------------------------------------

  /** Parse et valide une ligne brute "id;produit;quantite;prixUnitaire".
    *
    * Attendu : un for-comprehension qui enchaîne validerProduit,
    * validerQuantite et validerPrix, et construit une Commande en cas de
    * succès (yield). La première erreur rencontrée devient le résultat.
    */
  def parseCommande(ligne: String): Either[String, Commande] =
    ligne.split(";", -1) match
      case Array(id, produitBrut, quantiteBrute, prixBrut) =>
        for
          produit  <- validerProduit(produitBrut)
          quantite <- validerQuantite(quantiteBrute)
          prix     <- validerPrix(prixBrut)
        yield Commande(id, produit, quantite, prix, quantite * prix)
      case champs =>
        Left(s"Ligne mal formée : ${champs.length} champ(s) au lieu de 4 — '$ligne'")

  // ---------------------------------------------------------------------
  // Étape 4 — Traitement d'un lot de lignes
  // ---------------------------------------------------------------------

  /** Applique parseCommande à chaque ligne de la liste. */
  def traiterLignes(lignes: List[String]): List[Either[String, Commande]] =
    lignes.map(parseCommande)

  // ---------------------------------------------------------------------
  // Étape 5 — Extraction d'un message lisible avec fold
  // ---------------------------------------------------------------------

  /** Transforme un résultat en message lisible :
    *   - "[ERREUR] <message>" si Left
    *   - "[OK] <id> - <produit> - <quantite> x <prix> = <total>" si Right
    * Utilisez `.fold(...)`.
    */
  def resumerResultat(resultat: Either[String, Commande]): String =
    resultat.fold(
      erreur => s"[ERREUR] $erreur",
      commande =>
        s"[OK] ${commande.id} - ${commande.produit} - ${commande.quantite} x ${commande.prixUnitaire} = ${commande.montantTotal}"
    )

  // ---------------------------------------------------------------------
  // Étape 6 — Statistiques globales sur le lot
  // ---------------------------------------------------------------------

  /** Calcule le nombre de commandes valides, le nombre d'erreurs et le
    * montant total des commandes valides.
    * Indice : `partitionMap(identity)` sépare une List[Either[L, R]] en
    * un (List[L], List[R]).
    */
  def calculerStatistiques(resultats: List[Either[String, Commande]]): Statistiques =
    val (erreurs, valides) = resultats.partitionMap(identity)
    Statistiques(
      nbValides = valides.size,
      nbErreurs = erreurs.size,
      montantTotal = valides.map(_.montantTotal).sum
    )

end Pipeline

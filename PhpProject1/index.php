<!DOCTYPE html>
<?php
include_once './racine.php';
?>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Ajouter un étudiant</title>
    </head>
    <body>
        <!-- Form to add a new student -->
        <form method="GET" action="controller/addEtudiant.php">

            <fieldset>
                <legend>Ajouter un nouvel étudiant</legend>

                <table border="0">

                    <tr>
                        <td>Nom :</td>
                        <td><input type="text" name="nom" required /></td>
                    </tr>
                    <tr>
                        <td>Prénom :</td>
                        <td><input type="text" name="prenom" required /></td>
                    </tr>
                    <tr>
                        <td>Ville :</td>
                        <td>
                            <select name="ville" required>
                                <option value="Marrakech">Marrakech</option>
                                <option value="Rabat">Rabat</option>
                                <option value="Agadir">Agadir</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Sexe :</td>
                        <td>
                            <label>M</label><input type="radio" name="sexe" value="homme" required />
                            <label>F</label><input type="radio" name="sexe" value="femme" required />
                        </td>
                    </tr>
                    
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" value="Envoyer" />
                            <input type="reset" value="Effacer" />
                        </td>
                    </tr>

                </table>

            </fieldset>
        </form>

        <!-- Table to display students -->
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Ville</th>
                    <th>Sexe</th>
                    <th>Supprimer</th>
                    <th>Modifier</th>
                </tr>
            </thead>
            <tbody>
                <?php
                include_once RACINE . '/service/EtudiantService.php';
                $es = new EtudiantService();
                foreach ($es->findAll() as $e) {
                    ?>
                    <tr>
                        <td><?php echo $e->getId(); ?></td>
                        <td><?php echo $e->getNom(); ?></td>
                        <td><?php echo $e->getPrenom(); ?></td>
                        <td><?php echo $e->getVille(); ?></td>
                        <td><?php echo $e->getSexe(); ?></td>
                        <td>
                            <a href="controller/deleteEtudiant.php?id=<?php echo $e->getId(); ?>">Supprimer</a>
                        </td>
                        <td>
                            <a href="updateEtudiant.php?id=<?php echo $e->getId(); ?>">Modifier</a>
                        </td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>
    </body>
</html>
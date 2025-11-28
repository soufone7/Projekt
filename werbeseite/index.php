<?php global $name, $email, $lang, $gerichte, $anzNewsletter, $besucherGesamt, $agree;

/**
 * Praktikum DBWT. Autoren:
 * Zakaria, El Yamani, 3737198
 * Soufiane, Ait lahssaine, 3730735
 */
include __DIR__ . '/newsletter_handler.php';
include 'Statistiken-Optionen.php' ;
?>


<!DOCTYPE html>


<html lang="de">
<head>
    <link rel="stylesheet" href="index.css">
    <meta charset="UTF-8">
    <title>E-Mensa</title>
</head>
<body>
<header class="website-header">
    <img src="Mensa-logo.png" alt="logo">
    <nav id="Header">
        <!--
        Alle Links verweisen auf Bereiche (IDs) unten auf der Seite.
        Klicke -> springt zum Abschnitt.
         -->
        <ul>
            <a href="#ankündigung"><strong>Ankündigung</strong></a>
            <a href="#speisen"><strong>Speisen</strong></a>
            <a href="#zahlen"><strong>Zahlen</strong></a>
            <a href="#kontakt"><strong>Kontakt</strong></a>
            <a href="#wichtig"><strong>Wichtig für uns</strong></a>
        </ul>
    </nav>
</header>

<main>
    <img src="M.png" alt="bild"  id="myImage">
    <!-- ===== Ankündigung ===== -->
    <section id="ankündigung" aria-labelledby="ankündigung-titel">
        <h1 id="ankündigung-titel"> Bald gibt es Essen auch online ;)</h1>
        <p>
            Unsere E-Mensa geht bald online. Dann können Sie Gerichte bequem aussuchen, bestellen und zur gewünschten Zeit abholen.
            Wir kochen frisch, regional und zu fairen Preisen. Aktuelle Infos finden Sie hier auf der Seite.
        </p>
    </section>
    <!-- ===== Speisen ===== -->
    <section id="speisen" aria-labelledby="Speisen-titel">
        <div class="table-wrapper">
            <table>
                <caption id="Speisen-titel">Speisen und Preise</caption>

                <thead>
                <tr>
                    <th>Name</th>
                    <th>Preis intern</th>
                    <th>Preis extern</th>
                    <th>Allergene</th>
                </tr>
                </thead>

                <tbody >
                <!-- M3-A5  -->
                <?php
                $link = mysqli_connect("localhost", "root", "SouF", "emensawerbeseite");

                if (!$link) {
                    echo "Verbindung fehlgeschlagen: " , mysqli_connect_error(); exit();
                }

                // Sortierreihenfolge festlegen
                $sort = $_GET['sort'] ?? 'asc'; // 'asc' oder 'desc'
                $sort = ($sort === 'desc') ? 'DESC' : 'ASC';


                $sql = "SELECT id, name, preisintern, preisextern 
                    FROM gericht
                    ORDER BY name $sort
                    LIMIT 5";
                //result enthält ein Tabelle
                $result = mysqli_query($link, $sql);
                ?>
                <h2>Gerichte (<?php echo strtoupper($sort); ?>)</h2>
                <a href="?sort=asc">Aufsteigend</a> | <a href="?sort=desc">Absteigend</a>


                <?php
                //mysqli_fetch_assoc(): char indezes auch $row['id']
                //mysqli_fetch_row() :gibt numerische Indizes zurück $row[1]

                // $row ist jede Zeile in Tabelle/ id == gericht_Id
                $usedCodes = []; // NEU: hier alle verwendeten Codes sammeln
                while ($row = mysqli_fetch_assoc($result)) {
                    $gerichtID = (int)$row['id'];

                    // Allergene für dieses Gericht holen (nur Codes)
                    $alrg = "SELECT code FROM gericht_hat_allergen
                             where gericht_id= $gerichtID";
                    //Allergen als Datenbank-Result-Objekt (z.B. ['code' => 'a'] ['code' => 'g'] )
                    $resAl = mysqli_query($link, $alrg);

                    //die Codes auszulesen $codes = ['a', 'g'];
                    $codes = [];
                    while ($al = mysqli_fetch_assoc($resAl)) {
                        $codes[] = $al['code'];
                    }
                    //Ternary Operator: implode werbindet die code mit ","
                    $codeText = empty($codes) ? '-' : implode(', ', $codes);

                    // NEU: alle Codes ins globale Array übernehmen $codes=neu ,
                    $usedCodes = array_unique(array_merge($usedCodes, $codes));

                    echo '<tr>';
                    echo '<td>' . htmlspecialchars($row['name']) . '</td>';
                    echo '<td>' . htmlspecialchars($row['preisintern']) . ' €</td>';
                    echo '<td>' . htmlspecialchars($row['preisextern']) . ' €</td>';
                    echo '<td>'  . htmlspecialchars($codeText) . '</td>';
                    echo '</tr>';
                }
                ?>
                </tbody>
            </table>
            <?php
            // ============================
            // Allergene-Legende per JOIN
            // ============================

            if (!empty($usedCodes)) {
                //nur Prüfung
                $safeCodes = array_map(function($c) use ($link) {
                    return mysqli_real_escape_string($link, $c);
                }, $usedCodes);
                // Codes in SQL-Form bringen: 'A1','G2','L4'
                $inList = "'" . implode("','", $safeCodes) . "'";

                // JOIN: holt Code + Name aller verwendeten Allergene
                $sqlAll = "
                SELECT DISTINCT a.code, a.name
                 FROM allergen a
                JOIN gericht_hat_allergen gha ON a.code = gha.code
                WHERE a.code IN ($inList)
                ORDER BY a.code
                ";

                $resAll = mysqli_query($link, $sqlAll);

                echo '<ul class="allergen-legend">';

                while ($rowA = mysqli_fetch_assoc($resAll)) {
                    echo '<li class="allergen-item">'
                            . '<span class="allergen-code">' . htmlspecialchars($rowA['code'], ENT_QUOTES, 'UTF-8') . '</span>'
                            . '<span class="allergen-sep"> – </span>'
                            . '<span class="allergen-name">' . htmlspecialchars($rowA['name'], ENT_QUOTES, 'UTF-8') . '</span>'
                            . '</li>';
                }

                echo '</ul>';

                mysqli_free_result($resAll);
            }
            ?>
        </div>
    </section>

    <!-- ===== Zahlen ===== -->
    <section id="zahlen" aria-labelledby="Zahlen-titel">
        <h2 id="Zahlen-titel">E-Mensa in Zahlen</h2>
        <p>
            <strong><?= htmlspecialchars($besucherGesamt) ?></strong> Besuche
            <strong><?= htmlspecialchars($anzNewsletter) ?></strong> Anmeldungen zum Newsletter
            <strong><?= htmlspecialchars($gerichte) ?></strong> Speisen
        </p>
    </section>

    <!-- ===== Kontakt  ===== -->
    <section id="kontakt" aria-labelledby="Kontakt-titel">
        <h2 id="Kontakt-titel">Intesse geweckt? Wir informieren Sie!</h2>

        <!--A9 Bereich für Erfolg/Fehler -->
        <?php if (!empty($successMsg)): ?>
            <p style="color: #0a6;"><strong><?= htmlspecialchars($successMsg) ?></strong></p>
        <?php endif; ?>
        <?php if (!empty($errors)): ?>
            <ul style="color: #a00;">
                <?php foreach ($errors as $e): ?>
                    <li><?= htmlspecialchars($e) ?></li>
                <?php endforeach; ?>
            </ul>
        <?php endif; ?>

        <form action="#" method="post">
            <!-- Name -->
            <div>
                <label for="name">Ihr Name:</label><br>
                <input id="name" name="name" type="text" placeholder="Vorname"
                       value="<?= htmlspecialchars($name) ?>" required>
            </div>
            <!-- E-Mail -->
            <div>
                <label for="email">Ihre E-Mail:</label><br>
                <input id="email" name="email" type="email" placeholder="name@beispiel.de"
                       value="<?= htmlspecialchars($email) ?>" required>
            </div>
            <!-- Sprache Newsletter -->
            <div>
                <label for="sprache">Newsletter bitte in:</label>
                <select name="sprache" id="sprache">
                    <option <?= $lang==='Deutsch'?'selected':'' ?>>Deutsch</option>
                    <option <?= $lang==='Englisch'?'selected':'' ?>>Englisch</option>
                </select>
            </div>
            <!-- Datenschutz-Zustimmung -->
            <div>
                <input id="Da" name="Da" type="checkbox" <?= $agree?'checked':'' ?> required>
                <label for="Da">Den Datenschutzbestimmungen stimme ich zu.</label>
            </div>

            <p>
                <button type="submit" >
                    Zum Newsletter anmelden
                </button>
            </p>
        </form>
    </section>

    <!-- ===== Wichtig für uns ===== -->

    <section id="wichtig" aria-labelledby="wichtig-titel">
        <h2 id="wichtig-titel">Das ist uns wichtig</h2>
        <ul>
            <li>Beste frische saisonale Zutaten</li>
            <li>Ausgewagene abwechslungsreiche Gerichte</li>
            <li>Souberkeit</li>
        </ul>
    </section>

    <h1 id="Danke">Wir Freuen uns auf Ihren Besuch!</h1>

    <hr>

    <!-- =================== Footer =================== -->
    <footer>
        <p>
            <a href="#index.php">E-Mensa GmbH</a>
        </p>
        <p>
            Zakaria&soufiane
        </p>
        <p>
            <a href="#Impressum">Impressum</a>
        </p>
    </footer>

</main>
</body>
</html>

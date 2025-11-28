<?php

/* 9.1 – Anzahl Gerichte aus DB */

// Verbindung zur MySQL-Datenbank herstellen
$link = mysqli_connect("localhost", "root", "SouF", "emensawerbeseite");

// Falls die Verbindung fehlschlägt → Fehlermeldung ausgeben und abbrechen
if (!$link) {
    echo "DB-Fehler: " , mysqli_connect_error();
    exit();
}

// SQL-Abfrage zählt alle Zeilen in der Tabelle 'gericht'
$sqlGerichte = "SELECT COUNT(*) AS anzahl FROM gericht";

// SQL absetzen
$resGerichte = mysqli_query($link, $sqlGerichte);

// Falls die Abfrage fehlschlägt → Fehlermeldung ausgeben
if (!$resGerichte) {
    echo "SQL-Fehler: " , mysqli_error($link);
    exit();
}

// Ergebnis holen (als assoziatives Array)
$rowG = mysqli_fetch_assoc($resGerichte);

// Wert in Variable schreiben (typecast zu int)
$gerichte = (int)$rowG['anzahl'];

// Resultset freigeben (optional)
mysqli_free_result($resGerichte);


/* 9.2 – Anzahl Newsletter-Anmeldungen aus Datei */

// Pfad zur CSV-Datei
$newsletterDatei = __DIR__ . "/data/newsletter.csv";

$anzNewsletter = 0;

// Prüfen ob die Datei lesbar ist
if (is_readable($newsletterDatei)) {

    $fh = fopen($newsletterDatei, 'r');

    // Wenn Datei erfolgreich geöffnet wurde
    if ($fh) {
        // Datei Zeile für Zeile lesen
        while (($line = fgets($fh)) !== false) {

            // Leere Zeilen ignorieren
            if (trim($line) !== '') {
                $anzNewsletter++;
            }
        }
        fclose($fh);
    }

} else {
    // Datei existiert nicht → 0 Einträge
    $anzNewsletter = 0;
}


/* 9.3 – Besuch speichern + Gesamtzahl lesen */

// Falls $link nicht existiert oder ungültig ist → neue Verbindung aufbauen
if (!isset($link) || $link === false) {
    $link = mysqli_connect("localhost", "root", "SouF", "emensawerbeseite");

    if (!$link) {
        echo "DB-Fehler: " , mysqli_connect_error();
        exit();
    }
}

// Neuen Besuch speichern
// Tabelle 'besuch' hat wahrscheinlich nur ein Auto-Increment-Feld
$sqlInsert = "INSERT INTO besuch () VALUES ()";

// Insert ausführen
if (!mysqli_query($link, $sqlInsert)) {
    echo "Insert-Fehler: " , mysqli_error($link);
    exit();
}

// Gesamtzahl aller Besuche abfragen
$sqlBesuche = "SELECT COUNT(*) AS anzahl FROM besuch";

$resBesuche = mysqli_query($link, $sqlBesuche);

// Fehlerbehandlung
if (!$resBesuche) {
    echo "SQL-Fehler: " , mysqli_error($link);
    exit();
}

$rowB = mysqli_fetch_assoc($resBesuche);
$besucherGesamt = (int)$rowB['anzahl'];

mysqli_free_result($resBesuche);

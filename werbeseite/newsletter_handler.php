<?php
/**
 * Praktikum DBWT. Autoren:
 * Zakaria, El Yamani, 3737198
 * Soufiane, Ait lahssaine, 3730375
 */
// Erfolgsmeldung & Fehlerliste vorbereiten
$successMsg = '';
$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {

    // ---------------------------------------------------------
    // Eingaben aus dem Formular holen und trimmen
    // ---------------------------------------------------------
    $name  = trim($_POST['name']  ?? '');
    $email = trim($_POST['email'] ?? '');
    $lang  = trim($_POST['sprache'] ?? '');
    $agree = !empty($_POST['Da']); // Checkbox "Datenschutz" angehakt?

    // ---------------------------------------------------------
    // Validierungen
    // ---------------------------------------------------------

    // Name leer?
    if ($name === '') {
        $errors[] = 'Bitte geben Sie einen Namen ein.';
    }

    // Datenschutz nicht akzeptiert?
    if (!$agree) {
        $errors[] = 'Bitte stimmen Sie den Datenschutzbestimmungen zu.';
    }

    // Email gültig?
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $errors[] = 'Bitte geben Sie eine gültige E-Mail-Adresse ein.';
    } else {
        // Blockierte E-Mail-Domains
        $blocked = ['trashmail.de', 'trashmail.com', 'wegwerfmail.de'];

        // Domain extrahieren 1-links von @ 2- nur links ohne @ 3- klein schreiben
        $domain = strtolower(substr(strrchr($email, '@'), 1));

        // Prüfen, ob die Domain blockiert ist
        $isBlocked =
            in_array($domain, $blocked, true)
            || preg_match('/(^|\.)trashmail\./', $domain);

        if ($isBlocked) {
            $errors[] = 'E-Mail-Adressen von Wegwerf-Anbietern sind nicht erlaubt.';
        }
    }

    // ---------------------------------------------------------
    // Nur speichern, wenn keine Fehler vorhanden sind
    // ---------------------------------------------------------
    if (!$errors) {

        // Datenordner & Datei vorbereiten
        $dir  = __DIR__ . '/data';
        $file = $dir . '/newsletter.csv';

        // Ordner anlegen, falls nicht vorhanden
        if (!is_dir($dir)) {
            @mkdir($dir, 0775, true);
        }

        // Zeile, die in CSV geschrieben wird
        $row = [
            date('c'),                   // ISO-Datum
            $name,
            $email,
            $lang,
            $_SERVER['REMOTE_ADDR'] ?? '' // IP-Adresse
        ];

        // Datei im Append-Modus öffnen
        if ($fh = @fopen($file, 'ab')) {

            // Schreibsperre setzen
            if (flock($fh, LOCK_EX)) { // Sperren
                fputcsv($fh, $row, ';', '"', '\\'); // CSV schreiben
                fflush($fh);                       // Puffer leeren
                flock($fh, LOCK_UN);      // Sperre lösen
            }

            fclose($fh);

            // Erfolgsmeldung
            $successMsg = 'Vielen Dank! Ihre Newsletter-Anmeldung wurde gespeichert.';

            // Formularfelder leeren
            $name = $email = $lang = '';
            $agree = false;

        } else {
            // Fehler beim Öffnen der Datei
            $errors[] = 'Interner Fehler: Datei konnte nicht geschrieben werden.';
        }
    }
}

// ---------------------------------------------------------
// Refill-Werte für das Formular
// ---------------------------------------------------------
$name  = $name  ?? '';
$email = $email ?? '';
$lang  = $lang  ?? '';
$agree = !empty($agree);

?>
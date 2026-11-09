# language: pl

@Navigation
Funkcja: Nawigacja na stronie głównej

  Założenia:
    Zakładając, że użytkownik uruchomi stronę główną
    I użytkownik zaakceptuje "wszystkie"

  @GUI
  Szablon scenariusza: Przejście do sekcji "<pozycja>" z menu głównego
    Kiedy użytkownik wybierze w menu głównym "<pozycja>"
    Wtedy adres strony powinien mieć ścieżkę "<sciezka>"
    I tytuł strony powinien mieć wartość "<tytul>"

    # Znak | w tytule zapisujemy jako \| - inaczej Cucumber potraktuje go jako separator kolumn
    Przykłady:
      | pozycja         | sciezka          | tytul                                                    |
      | Premium         | /premium         | Oferta Premium \| ING Bank Śląski                        |
      | Private Banking | /private-banking | Oferta Private Banking dla klientów ING z aktywami 1 mln |
      | Przedsiębiorcy  | /male-firmy      | Bankowość dla małych firm \| ING Bank Śląski             |

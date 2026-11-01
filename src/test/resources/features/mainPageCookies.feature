# language: pl

Funkcja: Logowanie do aplikacji

  @GUI @Cookies @AllCookies
  Scenariusz: Weryfikacja zaakceptowanych Cookies wszystkich
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje wszystkie cookies
    Wtedy zaakceptowano wszystkie cookies

  @GUI @Cookies @Necessary
  Scenariusz: Weryfikacja zaakceptowanych niezbędnych Cookies
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje niezbędne cookies
    Wtedy zaakceptowano tylko niezbędne cookies

  @GUI @Cookies @Analytics
  Scenariusz: Weryfikacja zaakceptowanych Cookies analitycznych
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje "Cookies analityczne"
    Wtedy zaakceptowano analityczne cookies

  @GUI @Cookies @Marketing
  Scenariusz: Weryfikacja zaakceptowanych Cookies marketingowych
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje "Cookies marketingowe"
    Wtedy zaakceptowano marketingowe cookies

# language: pl

@Cookies
Funkcja: Logowanie do aplikacji

  @GUI @AllCookies
  Scenariusz: Weryfikacja zaakceptowanych Cookies wszystkich
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje wszystkie cookies
    Wtedy zaakceptowano wszystkie cookies

  @GUI @Necessary
  Scenariusz: Weryfikacja zaakceptowanych niezbędnych Cookies
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje niezbędne cookies
    Wtedy zaakceptowano tylko niezbędne cookies

  @GUI @Analytics
  Scenariusz: Weryfikacja zaakceptowanych Cookies analitycznych
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje "Cookies analityczne"
    Wtedy zaakceptowano analityczne cookies

  @GUI @Marketing
  Scenariusz: Weryfikacja zaakceptowanych Cookies marketingowych
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy zaakceptuje "Cookies marketingowe"
    Wtedy zaakceptowano marketingowe cookies

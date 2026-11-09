# language: pl

@Cookies
Funkcja: Zgody na pliki cookies na stronie głównej

  Założenia:
    Zakładając, że użytkownik uruchomi stronę główną

  @GUI
  Szablon scenariusza: Weryfikacja zapisanej zgody na cookies - <wybor>
    Kiedy użytkownik zaakceptuje "<wybor>"
    Wtedy zapisana zgoda na cookies to "<zgoda>"

    @AllCookies
    Przykłady: Wszystkie cookies
      | wybor     | zgoda |
      | wszystkie | ALL   |

    @Necessary
    Przykłady: Tylko niezbędne cookies
      | wybor           | zgoda     |
      | tylko niezbędne | NECESSARY |

    @Analytics
    Przykłady: Cookies analityczne
      | wybor               | zgoda     |
      | Cookies analityczne | ANALYTICS |

    @Marketing
    Przykłady: Cookies marketingowe
      | wybor                | zgoda     |
      | Cookies marketingowe | MARKETING |

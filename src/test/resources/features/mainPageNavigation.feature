# language: pl

@Navigation
Funkcja: Nawigacja na stronie głównej

  @GUI
  Scenariusz: Menu główne na stronie głównej
    Zakładając, że Użytkownik uruchomi stronę główną

    Kiedy w menu głównym wybierze "Premium"
    Wtedy Title na stronie powinno mieć wartość "Oferta Premium | ING Bank Śląski"

    Kiedy w menu głównym wybierze "Private Banking"
    Wtedy Title na stronie powinno mieć wartość "Oferta Private Banking dla klientów ING z aktywami 1 mln"

    Kiedy w menu głównym wybierze "Przedsiębiorcy"
    Wtedy Title na stronie powinno mieć wartość "Bankowość dla małych firm | ING Bank Śląski"


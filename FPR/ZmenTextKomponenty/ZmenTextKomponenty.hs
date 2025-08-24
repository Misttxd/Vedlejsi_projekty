data Komponenta
    = TextovePole {nazevKomponenty :: String, textKomponenty :: String}
    | Tlacitko  {nazevKomponenty :: String, hodnotaTlacitka :: String}
    | Kontejner {nazevKomponenty :: String, podrizeneKomponenty :: [Komponenta]}
    deriving(Show)

zmenTextKomponenty :: Komponenta -> String -> String -> Komponenta
zmenTextKomponenty (TextovePole aktualniNazev staryText) cilovyNazev novyText
    | aktualniNazev == cilovyNazev = TextovePole aktualniNazev novyText
    | otherwise = TextovePole aktualniNazev staryText
zmenTextKomponenty (Tlacitko aktualniNazev hodnota) _ _ = Tlacitko aktualniNazev hodnota
zmenTextKomponenty (Kontejner aktualniNazev podkomponenty) cilovyNazev novyText =
    Kontejner aktualniNazev (map (\podrizenaKomponenta -> zmenTextKomponenty podrizenaKomponenta cilovyNazev novyText) podkomponenty)

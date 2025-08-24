data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

odstranKomponentyPodleNazvu :: PrvekGUI -> [String] -> PrvekGUI
odstranKomponentyPodleNazvu (TextovePole aktualniNazev textKomponenty) nazvyKOdstraneni
    | aktualniNazev `elem` nazvyKOdstraneni = Kontejner "" []
    | otherwise = TextovePole aktualniNazev textKomponenty
odstranKomponentyPodleNazvu (Tlacitko aktualniNazev hodnotaKomponenty) nazvyKOdstraneni
    | aktualniNazev `elem` nazvyKOdstraneni = Kontejner "" []
    | otherwise = Tlacitko aktualniNazev hodnotaKomponenty
odstranKomponentyPodleNazvu (Kontejner aktualniNazev podrizenePrvky) nazvyKOdstraneni
    | aktualniNazev `elem` nazvyKOdstraneni = Kontejner aktualniNazev []
    | otherwise = Kontejner aktualniNazev (odstranKomponentyZPodrizenych podrizenePrvky nazvyKOdstraneni)
  where
    odstranKomponentyZPodrizenych :: [PrvekGUI] -> [String] -> [PrvekGUI]
    odstranKomponentyZPodrizenych [] _ = []
    odstranKomponentyZPodrizenych (prvniPrvek:zbytekPrvku) seznamNazvu = odstranKomponentyPodleNazvu prvniPrvek seznamNazvu : odstranKomponentyZPodrizenych zbytekPrvku seznamNazvu

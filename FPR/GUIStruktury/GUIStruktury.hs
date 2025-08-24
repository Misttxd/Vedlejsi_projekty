data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

spocitejTlacitka :: PrvekGUI -> Int
spocitejTlacitka (Tlacitko _ _) = 1
spocitejTlacitka (TextovePole _ _) = 0
spocitejTlacitka (Kontejner _ podkomponenty) = spocitejTlacitkaVSeznamu podkomponenty
  where
    spocitejTlacitkaVSeznamu :: [PrvekGUI] -> Int
    spocitejTlacitkaVSeznamu [] = 0
    spocitejTlacitkaVSeznamu (prvniKomponenta:zbytekKomponent) = spocitejTlacitka prvniKomponenta + spocitejTlacitkaVSeznamu zbytekKomponent

pridejPrvekDoKontejneru :: PrvekGUI -> PrvekGUI -> String -> PrvekGUI
pridejPrvekDoKontejneru strukturaGUI novyPrvek cilovyNazevKontejneru = pridejDoKontejneruRekurzivne strukturaGUI
  where
    pridejDoKontejneruRekurzivne (Kontejner aktualniNazev podkomponenty)
      | aktualniNazev == cilovyNazevKontejneru = Kontejner aktualniNazev (podkomponenty ++ [novyPrvek])
      | otherwise = Kontejner aktualniNazev (map pridejDoKontejneruRekurzivne podkomponenty)
    pridejDoKontejneruRekurzivne jinaKomponenta = jinaKomponenta

seznamJmenTlacitek :: PrvekGUI -> [String]
seznamJmenTlacitek (Tlacitko nazev _) = [nazev]
seznamJmenTlacitek (TextovePole _ _) = []
seznamJmenTlacitek (Kontejner _ podkomponenty) = seznamJmenTlacitekVSeznamu podkomponenty
  where
    seznamJmenTlacitekVSeznamu :: [PrvekGUI] -> [String]
    seznamJmenTlacitekVSeznamu [] = []
    seznamJmenTlacitekVSeznamu (prvniKomponenta:zbytekKomponent) = seznamJmenTlacitek prvniKomponenta ++ seznamJmenTlacitekVSeznamu zbytekKomponent

zmenTextKomponenty :: PrvekGUI -> String -> String -> PrvekGUI
zmenTextKomponenty (TextovePole aktualniNazev staryText) cilovyNazev novyText
    | aktualniNazev == cilovyNazev = TextovePole aktualniNazev novyText
    | otherwise = TextovePole aktualniNazev staryText
zmenTextKomponenty (Tlacitko aktualniNazev hodnota) _ _ = Tlacitko aktualniNazev hodnota
zmenTextKomponenty (Kontejner aktualniNazev podkomponenty) cilovyNazev novyText =
    Kontejner aktualniNazev (map (\podrizenaKomponenta -> zmenTextKomponenty podrizenaKomponenta cilovyNazev novyText) podkomponenty)

seznamVsechNazvu :: PrvekGUI -> [String]
seznamVsechNazvu (TextovePole nazev _) = [nazev]
seznamVsechNazvu (Tlacitko nazev _) = [nazev]
seznamVsechNazvu (Kontejner nazev podrizenePrvky) = nazev : seznamNazvuZPodrizenych podrizenePrvky
  where
    seznamNazvuZPodrizenych :: [PrvekGUI] -> [String]
    seznamNazvuZPodrizenych [] = []
    seznamNazvuZPodrizenych (prvniPrvek:zbytekPrvku) = seznamVsechNazvu prvniPrvek ++ seznamNazvuZPodrizenych zbytekPrvku

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

seznamVsechTlacitek :: PrvekGUI -> [PrvekGUI]
seznamVsechTlacitek (Tlacitko nazev popisek) = [Tlacitko nazev popisek]
seznamVsechTlacitek (TextovyPrvek _) = []
seznamVsechTlacitek (Panel podrizenePrvky) = seznamTlacitekZPodrizenych podrizenePrvky
  where
    seznamTlacitekZPodrizenych :: [PrvekGUI] -> [PrvekGUI]
    seznamTlacitekZPodrizenych [] = []
    seznamTlacitekZPodrizenych (prvniPrvek:zbytekPrvku) = seznamVsechTlacitek prvniPrvek ++ seznamTlacitekZPodrizenych zbytekPrvku

odstranVsechnaTlacitka :: PrvekGUI -> PrvekGUI
odstranVsechnaTlacitka (Tlacitko _ _) = Panel []
odstranVsechnaTlacitka (TextovyPrvek popisek) = TextovyPrvek popisek
odstranVsechnaTlacitka (Panel podrizenePrvky) = Panel (odstranTlacitkaZPodrizenych podrizenePrvky)
  where
    odstranTlacitkaZPodrizenych :: [PrvekGUI] -> [PrvekGUI]
    odstranTlacitkaZPodrizenych [] = []
    odstranTlacitkaZPodrizenych (prvniPrvek:zbytekPrvku) = odstranVsechnaTlacitka prvniPrvek : odstranTlacitkaZPodrizenych zbytekPrvku

vytiskniCestu :: PrvekGUI -> String -> String
vytiskniCestu (TextovePole aktualniNazev _) cilovyNazev
    | aktualniNazev == cilovyNazev = aktualniNazev
    | otherwise = ""
vytiskniCestu (Tlacitko aktualniNazev _) cilovyNazev
    | aktualniNazev == cilovyNazev = aktualniNazev
    | otherwise = ""
vytiskniCestu (Kontejner aktualniNazev podrizenePrvky) cilovyNazev =
    najdiCestuVPodrizenych podrizenePrvky aktualniNazev
  where
    najdiCestuVPodrizenych :: [PrvekGUI] -> String -> String
    najdiCestuVPodrizenych [] _ = ""
    najdiCestuVPodrizenych (prvniPrvek:zbytekPrvku) rodicovskyNazev =
        let nalezenaCesta = vytiskniCestu prvniPrvek cilovyNazev
        in if nalezenaCesta /= ""
           then rodicovskyNazev ++ " / " ++ nalezenaCesta
           else najdiCestuVPodrizenych zbytekPrvku rodicovskyNazev

odstranKomponentuZKontejneruNaIndexu :: PrvekGUI -> PrvekGUI -> String -> Int -> PrvekGUI
odstranKomponentuZKontejneruNaIndexu (Kontejner aktualniNazevKontejneru podrizenePrvky) novaKomponenta cilovyNazevKontejneru cilovyIndex
    | aktualniNazevKontejneru == cilovyNazevKontejneru = Kontejner aktualniNazevKontejneru (vlozNaIndex podrizenePrvky novaKomponenta cilovyIndex)
    | otherwise = Kontejner aktualniNazevKontejneru (pridejDoPodrizenychRekurzivne podrizenePrvky)
  where
    pridejDoPodrizenychRekurzivne :: [PrvekGUI] -> [PrvekGUI]
    pridejDoPodrizenychRekurzivne [] = []
    pridejDoPodrizenychRekurzivne (prvniPrvek:zbytekPrvku) = pridejKomponentuDoKontejneruNaIndexu prvniPrvek novaKomponenta cilovyNazevKontejneru cilovyIndex : pridejDoPodrizenychRekurzivne zbytekPrvku

    vlozNaIndex :: [PrvekGUI] -> PrvekGUI -> Int -> [PrvekGUI]
    vlozNaIndex [] vkladanaKomponenta _ = [vkladanaKomponenta]
    vlozNaIndex (prvniPrvek:zbytekPrvku) vkladanaKomponenta aktualniIndex
      | aktualniIndex == 0 = vkladanaKomponenta : prvniPrvek : zbytekPrvku
      | otherwise = prvniPrvek : vlozNaIndex zbytekPrvku vkladanaKomponenta (aktualniIndex - 1)
odstranKomponentuZKontejneruNaIndexu aktualniKomponenta _ _ _ = aktualniKomponenta

spocitejVyskytKomponent :: PrvekGUI -> (Int, Int, Int)
spocitejVyskytKomponent (TextovePole _ _) = (1, 0, 0)
spocitejVyskytKomponent (Tlacitko _ _) = (0, 1, 0)
spocitejVyskytKomponent (Kontejner _ podrizenePrvky) = spocitejPodrizeneKomponenty podrizenePrvky
  where
    spocitejPodrizeneKomponenty :: [PrvekGUI] -> (Int, Int, Int)
    spocitejPodrizeneKomponenty [] = (0, 0, 1)
    spocitejPodrizeneKomponenty (prvniPrvek:zbytekPrvku) =
        let (textovaPole1, tlacitka1, kontejnery1) = spocitejVyskytKomponent prvniPrvek
            (textovaPole2, tlacitka2, kontejnery2) = spocitejPodrizeneKomponenty zbytekPrvku
        in (textovaPole1 + textovaPole2, tlacitka1 + tlacitka2, kontejnery1 + kontejnery2 + 1)

pridejKomponentuDoKontejneruNaIndexu :: PrvekGUI -> PrvekGUI -> String -> Int -> PrvekGUI
pridejKomponentuDoKontejneruNaIndexu (Kontejner aktualniNazevKontejneru podrizenePrvky) novaKomponenta cilovyNazevKontejneru cilovyIndex
    | aktualniNazevKontejneru == cilovyNazevKontejneru = Kontejner aktualniNazevKontejneru (vlozNaIndex podrizenePrvky novaKomponenta cilovyIndex)
    | otherwise = Kontejner aktualniNazevKontejneru (pridejDoPodrizenychRekurzivne podrizenePrvky)
  where
    pridejDoPodrizenychRekurzivne :: [PrvekGUI] -> [PrvekGUI]
    pridejDoPodrizenychRekurzivne [] = []
    pridejDoPodrizenychRekurzivne (prvniPrvek:zbytekPrvku) = pridejKomponentuDoKontejneruNaIndexu prvniPrvek novaKomponenta cilovyNazevKontejneru cilovyIndex : pridejDoPodrizenychRekurzivne zbytekPrvku

    vlozNaIndex :: [PrvekGUI] -> PrvekGUI -> Int -> [PrvekGUI]
    vlozNaIndex [] vkladanaKomponenta _ = [vkladanaKomponenta]
    vlozNaIndex (prvniPrvek:zbytekPrvku) vkladanaKomponenta aktualniIndex
      | aktualniIndex == 0 = vkladanaKomponenta : prvniPrvek : zbytekPrvku
      | otherwise = prvniPrvek : vlozNaIndex zbytekPrvku vkladanaKomponenta (aktualniIndex - 1)
pridejKomponentuDoKontejneruNaIndexu aktualniKomponenta _ _ _ = aktualniKomponenta

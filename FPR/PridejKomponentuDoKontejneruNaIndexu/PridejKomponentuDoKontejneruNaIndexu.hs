data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

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

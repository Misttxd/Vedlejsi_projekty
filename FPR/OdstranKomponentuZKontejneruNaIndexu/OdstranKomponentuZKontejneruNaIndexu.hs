data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

odstranKomponentuZKontejneruNaIndexu :: PrvekGUI -> String -> Int -> PrvekGUI
odstranKomponentuZKontejneruNaIndexu (Kontejner aktualniNazevKontejneru podrizenePrvky) cilovyNazevKontejneru cilovyIndex
    | aktualniNazevKontejneru == cilovyNazevKontejneru = Kontejner aktualniNazevKontejneru (odstranNaIndexu podrizenePrvky cilovyIndex)
    | otherwise = Kontejner aktualniNazevKontejneru (odstranZPodrizenychRekurzivne podrizenePrvky)
  where
    odstranNaIndexu :: [PrvekGUI] -> Int -> [PrvekGUI]
    odstranNaIndexu [] _ = []
    odstranNaIndexu (prvniPrvek:zbytekPrvku) aktualniIndex
      | aktualniIndex == 0 = zbytekPrvku
      | otherwise = prvniPrvek : odstranNaIndexu zbytekPrvku (aktualniIndex - 1)

    odstranZPodrizenychRekurzivne :: [PrvekGUI] -> [PrvekGUI]
    odstranZPodrizenychRekurzivne [] = []
    odstranZPodrizenychRekurzivne (prvniPrvek:zbytekPrvku) = 
        odstranKomponentuZKontejneruNaIndexu prvniPrvek cilovyNazevKontejneru cilovyIndex : odstranZPodrizenychRekurzivne zbytekPrvku
odstranKomponentuZKontejneruNaIndexu aktualniKomponenta _ _ = aktualniKomponenta

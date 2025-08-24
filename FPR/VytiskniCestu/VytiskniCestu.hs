data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

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

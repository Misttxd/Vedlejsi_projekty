data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

seznamVsechNazvu :: PrvekGUI -> [String]
seznamVsechNazvu (TextovePole nazev _) = [nazev]
seznamVsechNazvu (Tlacitko nazev _) = [nazev]
seznamVsechNazvu (Kontejner nazev podrizenePrvky) = nazev : seznamNazvuZPodrizenych podrizenePrvky
  where
    seznamNazvuZPodrizenych :: [PrvekGUI] -> [String]
    seznamNazvuZPodrizenych [] = []
    seznamNazvuZPodrizenych (prvniPrvek:zbytekPrvku) = seznamVsechNazvu prvniPrvek ++ seznamNazvuZPodrizenych zbytekPrvku

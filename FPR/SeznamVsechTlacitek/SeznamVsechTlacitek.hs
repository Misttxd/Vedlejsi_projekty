data PrvekGUI
    = Tlacitko {nazev :: String, popisek :: String}
    | TextovyPrvek {popisek :: String}
    | Panel {podrizenePrvky :: [PrvekGUI]}
    deriving(Show)

seznamVsechTlacitek :: PrvekGUI -> [PrvekGUI]
seznamVsechTlacitek (Tlacitko nazev popisek) = [Tlacitko nazev popisek]
seznamVsechTlacitek (TextovyPrvek _) = []
seznamVsechTlacitek (Panel podrizenePrvky) = seznamTlacitekZPodrizenych podrizenePrvky
  where
    seznamTlacitekZPodrizenych :: [PrvekGUI] -> [PrvekGUI]
    seznamTlacitekZPodrizenych [] = []
    seznamTlacitekZPodrizenych (prvniPrvek:zbytekPrvku) = seznamVsechTlacitek prvniPrvek ++ seznamTlacitekZPodrizenych zbytekPrvku

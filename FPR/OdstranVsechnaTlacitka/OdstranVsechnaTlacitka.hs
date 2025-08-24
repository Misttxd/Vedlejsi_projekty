data PrvekGUI
    = Tlacitko {nazev :: String, popisek :: String}
    | TextovyPrvek {popisek :: String}
    | Panel {podrizenePrvky :: [PrvekGUI]}
    deriving(Show)

odstranVsechnaTlacitka :: PrvekGUI -> PrvekGUI
odstranVsechnaTlacitka (Tlacitko _ _) = Panel []
odstranVsechnaTlacitka (TextovyPrvek popisek) = TextovyPrvek popisek
odstranVsechnaTlacitka (Panel podrizenePrvky) = Panel (odstranTlacitkaZPodrizenych podrizenePrvky)
  where
    odstranTlacitkaZPodrizenych :: [PrvekGUI] -> [PrvekGUI]
    odstranTlacitkaZPodrizenych [] = []
    odstranTlacitkaZPodrizenych (prvniPrvek:zbytekPrvku) = odstranVsechnaTlacitka prvniPrvek : odstranTlacitkaZPodrizenych zbytekPrvku

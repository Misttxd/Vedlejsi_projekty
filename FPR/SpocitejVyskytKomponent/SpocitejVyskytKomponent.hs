data PrvekGUI
    = TextovePole {nazev :: String, text :: String}
    | Tlacitko {nazev :: String, hodnota :: String}
    | Kontejner {nazev :: String, podrizenePrvky :: [PrvekGUI]}
    deriving (Show)

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

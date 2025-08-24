data Komponenta
    = Tlacitko String String
    | TextovePole String String
    | Kontejner String [Komponenta]
    deriving (Show)

spocitejTlacitka :: Komponenta -> Int
spocitejTlacitka (Tlacitko _ _) = 1
spocitejTlacitka (TextovePole _ _) = 0
spocitejTlacitka (Kontejner _ podkomponenty) = spocitejTlacitkaVSeznamu podkomponenty
  where
    spocitejTlacitkaVSeznamu :: [Komponenta] -> Int
    spocitejTlacitkaVSeznamu [] = 0
    spocitejTlacitkaVSeznamu (prvniKomponenta:zbytekKomponent) = spocitejTlacitka prvniKomponenta + spocitejTlacitkaVSeznamu zbytekKomponent

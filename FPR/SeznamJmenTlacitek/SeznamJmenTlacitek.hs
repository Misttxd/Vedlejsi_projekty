data Komponenta
    = TextovePole {nazevKomponenty :: String, textKomponenty :: String}
    | Tlacitko  {nazevKomponenty :: String, hodnotaTlacitka :: String}
    | Kontejner {nazevKomponenty :: String, podrizeneKomponenty :: [Komponenta]}
    deriving(Show)

seznamJmenTlacitek :: Komponenta -> [String]
seznamJmenTlacitek (Tlacitko nazev _) = [nazev]
seznamJmenTlacitek (TextovePole _ _) = []
seznamJmenTlacitek (Kontejner _ podkomponenty) = seznamJmenTlacitekVSeznamu podkomponenty
  where
    seznamJmenTlacitekVSeznamu :: [Komponenta] -> [String]
    seznamJmenTlacitekVSeznamu [] = []
    seznamJmenTlacitekVSeznamu (prvniKomponenta:zbytekKomponent) = seznamJmenTlacitek prvniKomponenta ++ seznamJmenTlacitekVSeznamu zbytekKomponent

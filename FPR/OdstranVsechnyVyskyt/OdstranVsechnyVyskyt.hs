odstranVsechnyVyskyt :: Eq a => a -> [a] -> [a]
odstranVsechnyVyskyt _ [] = []
odstranVsechnyVyskyt prvekKOdstraneni (aktualniPrvek:zbytekSeznamu)
  | aktualniPrvek == prvekKOdstraneni = odstranVsechnyVyskyt prvekKOdstraneni zbytekSeznamu
  | otherwise                         = aktualniPrvek : odstranVsechnyVyskyt prvekKOdstraneni zbytekSeznamu

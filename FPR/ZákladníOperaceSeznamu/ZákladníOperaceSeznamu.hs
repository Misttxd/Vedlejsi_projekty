delkaSeznamu :: [a] -> Int
delkaSeznamu [] = 0
delkaSeznamu (_:zbytekSeznamu) = 1 + delkaSeznamu zbytekSeznamu

sectiPrvkySeznamu :: Num a => [a] -> a
sectiPrvkySeznamu [] = 0
sectiPrvkySeznamu (prvniPrvek:zbytekSeznamu) = prvniPrvek + sectiPrvkySeznamu zbytekSeznamu

obratSeznam :: [a] -> [a]
obratSeznam [] = []
obratSeznam (prvniPrvek:zbytekSeznamu) = obratSeznam zbytekSeznamu ++ [prvniPrvek]

najdiMaximum :: [Int] -> Int
najdiMaximum [] = error "Seznam je prázdný"
najdiMaximum [prvni] = prvni
najdiMaximum (prvni:druhy:zbytek)
  | prvni > druhy = najdiMaximum (prvni:zbytek)
  | otherwise     = najdiMaximum (druhy:zbytek)

najdiMinimum :: [Int] -> Int
najdiMinimum [] = error "Seznam je prázdný"
najdiMinimum [prvni] = prvni
najdiMinimum (prvni:druhy:zbytek)
  | prvni < druhy = najdiMinimum (prvni:zbytek)
  | otherwise     = najdiMinimum (druhy:zbytek)

odstranVsechnyVyskyt :: Eq a => a -> [a] -> [a]
odstranVsechnyVyskyt _ [] = []
odstranVsechnyVyskyt prvekKOdstraneni (aktualniPrvek:zbytekSeznamu)
  | aktualniPrvek == prvekKOdstraneni = odstranVsechnyVyskyt prvekKOdstraneni zbytekSeznamu
  | otherwise                         = aktualniPrvek : odstranVsechnyVyskyt prvekKOdstraneni zbytekSeznamu

opakujPrvek :: Int -> a -> [a]
opakujPrvek 0 _ = []
opakujPrvek pocetOpakovani prvekKOakovani = prvekKOakovani : opakujPrvek (pocetOpakovani - 1) prvekKOakovani

sjednoceniSeznamu :: Eq a => [a] -> [a] -> [a]
sjednoceniSeznamu prvniSeznam druhySeznam = prvniSeznam ++ [prvek | prvek <- druhySeznam, not (elem prvek prvniSeznam)]

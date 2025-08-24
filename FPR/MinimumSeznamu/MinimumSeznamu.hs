najdiMinimum :: [Int] -> Int
najdiMinimum [] = error "Seznam je prázdný"
najdiMinimum [prvni] = prvni
najdiMinimum (prvni:druhy:zbytek)
  | prvni < druhy = najdiMinimum (prvni:zbytek)
  | otherwise     = najdiMinimum (druhy:zbytek)

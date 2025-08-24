najdiMaximum :: [Int] -> Int
najdiMaximum [] = error "Seznam je prázdný"
najdiMaximum [prvni] = prvni
najdiMaximum (prvni:druhy:zbytek)
  | prvni > druhy = najdiMaximum (prvni:zbytek)
  | otherwise     = najdiMaximum (druhy:zbytek)

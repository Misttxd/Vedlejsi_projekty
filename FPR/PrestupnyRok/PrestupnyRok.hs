jePrestupnyRok :: Int -> Bool
jePrestupnyRok rok = (rok `mod` 4 == 0 && rok `mod` 100 /= 0) || (rok `mod` 400 == 0)

-- 4 : [1,2,3,4] prida prvok na zaciatok
--   ^

length2 :: [a] -> Int
length2 [] = 0 -- prazdne pole
length2 [x] = 1 -- jednoprvkovy seznam
length2 (hlava:zbytek) =  1 + length2 zbytek

sumIt :: Num a => [a] -> a --funkce ktora vie scitat nie len Int ale aj double float Booll etc.
sumIt [] = 0
sumIt (hlava:zbytek) =  hlava + sumIt zbytek

merge :: Ord a => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs)(y:ys)  | x <= y = x : merge xs (y: ys)
                    | otherwise = y : merge (x:xs) ys

getHead :: [a] -> a
getHead [] = error "empty list"
getHead (hlava:zbytek) = hlava

getLast :: [a] -> a 
getLast[x] = x
getLast(hlave:zbytek) = getLast zbytek

isElement :: Eq a => a -> [a] -> Bool
isElement x [] = False
isElement x (hlava:zbytek) | (x == hlava) = True
                           | otherwise = isElement x zbytek
getInit :: [a] -> [a]
getInit[x] = []
getInit(x:xs) = x : getInit xs

combine :: [a] -> [a] -> [a]
combine [] y = y
combine (x:xs) y  = x : combine xs y

combine2 :: [a] -> [a] -> [a] 
combine2 x y  = x ++ y

max' :: [Int] -> Int
max' [] = error "empty list"
max' [x] = x
max' (x:y:z)| x > y = max' (x:z)
            | otherwise = max' (y:z)

reverse' :: [a] -> [a]
reverse' [] = []
reverse' (x:xs) = reverse' xs ++ [x]  

scalar :: [Int] -> [Int] -> Int
scalar [] [] = 0
scalar (x:xs) (y:ys) = x * y + scalar xs ys

nonZero :: [Int] -> [Int]
nonZero [] = []
nonZero (x:xs) | x == 0 = nonZero xs
               | otherwise = x : nonZero xs
 
moveLeft :: [Int] -> [Int]
moveLeft [] = []
moveLeft (x:xs) = xs ++ [x]

moveRight :: [Int] -> [Int]
moveRight [] = []
moveRight (x:xs) = [last xs] ++ [x] ++ init xs
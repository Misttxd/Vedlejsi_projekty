import Data.Char
import Distribution.Simple.Build (repl)

ntice :: [(String, Bool)] -> Int
ntice [] = 0
ntice [(x,y)] = 1

zipThem :: [a] -> [b] -> [(a,b)]
zipThem [] [] = [] 
zipThem (x:xs) (y:ys) = (x,y) : zipThem xs ys

splitWith :: Eq a => [a] -> [a] -> [[a]]
splitWith [] _ = []
splitWith xs delimiters = case dropWhile (`elem` delimiters) xs of
    [] -> []
    xs' -> segment : splitWith rest delimiters
      where
        (segment, rest) = break (`elem` delimiters) xs'

allToUpper :: String -> String
allToUpper [] = []
allToUpper (x:xs) = toUpper x : allToUpper xs

allToUpper2 :: String -> String
allToUpper2 xs = [toUpper x | x <- xs]

allToUpper3 :: String -> String
allToUpper3 xs = map toUpper xs


--Generator Seznamu: [(x,y)| x <- xs, y <- ys]

quicksort :: (Ord a) => [a] -> [a]
quicksort [] = []
quicksort (x:xs) = quicksort [y | y <- xs, y <= x] ++ [x] ++ quicksort [y | y <- xs, y > x]


removeOne :: Eq a => a -> [a] -> [a]
removeOne _ [] = []
removeOne x (y:ys) | x == y = ys
                   | otherwise = y : removeOne x ys

removeAll :: Eq a => a -> [a] -> [a]
removeAll _ [] = []
removeAll y (x:xs)| x /= y = x : removeAll y xs
                  | otherwise = removeAll y xs

replicate' :: Int -> a -> [a]
replicate' 0 y = []
replicate' x y = y : replicate' (x-1) y

rotateLeftN :: [a] -> Int -> [a]
rotateLeftN [] 0 = []
rotateLeftN (x:xs) y = xs ++ [x]



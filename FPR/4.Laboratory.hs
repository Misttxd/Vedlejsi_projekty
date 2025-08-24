import Distribution.Simple.Utils (xargs)
import GHC.Float (divideDouble)
import Data.ByteString (count)

take' :: Int -> [Int] -> [Int]
take' 0 _ = []
take' n (x:xs) = x : take' (n-1) xs

replaceByRepeat :: String -> Char -> Int -> String
replaceByRepeat str ch n = concatMap replaceChar str
  where
    replaceChar c
      | c == ch   = replicate n c
      | otherwise = [c]

drop' :: Int -> [Int] -> [Int]
drop' 0 xs = xs
drop' n (x:xs) = drop' (n-1) xs

accounts :: [(String, Int)] -> [String]
accounts xs= [acc | (acc, balance) <- xs, balance > 0]

minimum' :: [Int] -> Int
minimum' [] = error "empty list"
minimum' [x] = x
minimum'(x:y:z)| x < y = minimum'(x:z)
               | otherwise = minimum' (y:z)

divisors :: Int -> [Int]
divisors 0 = []
divisors n = tmp 1 where
    tmp x | n `mod` x == 0 = x : tmp (x+1)
          | x < n  = tmp (x+1) 
          |otherwise = divisors 0

zipThem :: [a] -> [b] -> [(a,b)]
zipThem [] [] = []
zipThem (x:xs) (y:ys) = (x,y) : zipThem xs ys

dotProduct :: [a] -> [b] -> [(a,b)]
dotProduct [] [] = []
dotProduct xs ys = [(x,y)|x<-xs, y<-ys]

sumPositive :: [Int] -> Int
sumPositive [] = 0
sumPositive (x:xs)| x > 0 = x + sumPositive xs
                  | otherwise = sumPositive xs

countIt :: Int -> [Int] -> Int
countIt n [] = 0
countIt n (x:xs)| n == x = 1 + countIt n xs
                | otherwise = countIt n xs 

listOdd :: Int -> Int -> [Int]
listOdd 0 0 = []
listOdd x y | x <= y = x : listOdd (x+2) y
            | otherwise = listOdd 0 0
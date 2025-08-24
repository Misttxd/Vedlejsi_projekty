
-- :info 
-- :type

-- sqrt 
-- succ INT, string, char za zadanym cislom  a = 1 -> 2, b = 'a' -> 'b'
-- pred INT, string, char pred zadanym cislom
-- odd Je parne/neparne
-- ceiling Zaokruhli hore
-- truncate Zaokrohli dole
-- round 
-- floor
-- mod
-- fromIntegral pretypovanie na INT

factorial :: Int -> Int
factorial  0 = 1

factorial n = n * factorial (n- 1)

factorials :: Int -> Int -> [(Int, Int)]
factorials a b = [(n, factorial n) | n <- [a..b]] 
                where
                    factorial 0 = 1
                    factorial n = n * factorial (n-1)

fact2 :: Int -> Int
fact2 n | (n == 0) = 1
        | otherwise = fact2 n * fact2(n-1)

fact3 :: Int -> Int 
fact3 n  = if n == 0 then 1 else n * fact3( n- 1)

-----------------------------------------------------------------------------------------------------------

complement :: Int -> Int -> [Int] -> [Int]
complement from to xs = [x | x <-[from..to], not (elem x xs)]

fib :: Int -> Int
fib 0 = 1
fib 1 = 1
fib x = fib(x-2) + fib (x-1)

fib2 :: Int -> Int 
fib2 x | (x == 0) = 1
       | (x == 1) = 1
       | otherwise = fib2 (x-2) + fib2(x-1)

fib3 :: Int -> Int
fib3 x = pomocna 1 1 0 where 
    pomocna a b i | (i  == x) = a
                  | otherwise = pomocna b (a+b) (i+1)

leapYear :: Int -> Bool
leapYear year = if mod year 4 == 0 && mod year 100 /= 0 then False else True


max2 :: Int -> Int -> Int 
max2 x y | x >= y = x
         | otherwise = y

max3 :: Int -> Int -> Int -> Int
max3 x y z | x > z || x > y = x
           | z > x || z > y = z
           | otherwise = y

countries :: [(String, Int)] -> Int -> [String]
countries list threshold = [name | (name, population) <- list, population > threshold]

numberOfRoots :: Int -> Int -> Int -> Int
numberOfRoots a b c = let d = b * b - 4 * a * c 
                      in if d < 0 then 0 else if d == 0 then 1 else 2   

gcd2 :: Int -> Int -> Int
gcd2 a b | a == b = a
         | a > b = gcd2 (a-b) b
         | a < b = gcd2 a (b-a) 

convert :: [(String, Int, Float)] -> [(String, Float)]
convert xs = [(name, fromIntegral amount * price)| (name, amount, price) <- xs]

isPrime :: Int -> Bool
isPrime 1 = False
isPrime y = isPrimeTest y (y-1) where
        isPrimeTest _ 1 = True
        isPrimeTest n x | n `mod` x == 0 = False
                        | otherwise = isPrimeTest n (x-1)        

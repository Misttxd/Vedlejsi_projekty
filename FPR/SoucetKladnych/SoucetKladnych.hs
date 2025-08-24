sectiKladnePrvky :: [Int] -> Int
sectiKladnePrvky [] = 0
sectiKladnePrvky (prvniPrvek:zbytekSeznamu)
  | prvniPrvek > 0 = prvniPrvek + sectiKladnePrvky zbytekSeznamu
  | otherwise      = sectiKladnePrvky zbytekSeznamu

sectiPrvkySeznamu :: Num a => [a] -> a
sectiPrvkySeznamu [] = 0
sectiPrvkySeznamu (prvniPrvek:zbytekSeznamu) = prvniPrvek + sectiPrvkySeznamu zbytekSeznamu

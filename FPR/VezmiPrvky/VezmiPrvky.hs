vezmiPrvky :: Int -> [a] -> [a]
vezmiPrvky 0 _ = []
vezmiPrvky pocet (prvniPrvek:zbytekSeznamu) = prvniPrvek : vezmiPrvky (pocet - 1) zbytekSeznamu

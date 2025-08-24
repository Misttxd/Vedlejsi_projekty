vypocitejPrumerVeku :: [(String, Int)] -> Double
vypocitejPrumerVeku seznamOsob = fromIntegral (sum seznamVeku) / fromIntegral (length seznamVeku)
  where
    seznamVeku = map snd seznamOsob

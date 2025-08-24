type Bludiste = [String]

ziskejCastBludiste :: Bludiste -> (Int, Int) -> (Int, Int) -> Bludiste
ziskejCastBludiste puvodniBludiste (pocatecniRadek, pocatecniSloupec) (vyska, sirka) =
  map (take sirka . drop pocatecniSloupec) (take vyska . drop pocatecniRadek $ puvodniBludiste)

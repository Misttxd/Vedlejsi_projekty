data TernaryTree a
    = Leaf a                           
    | Node (TernaryTree a) (TernaryTree a) (TernaryTree a)
    deriving (Show)


exampleTree :: TernaryTree Int
exampleTree = Node
    (Leaf 1)
    (Node (Leaf 2) (Leaf 3) (Leaf 4))
    (Leaf 5)

data Component
    = TextBox { name :: String, text :: String }
    | Button { name :: String, value :: String }
    | Container { name :: String, children :: [Component] }
    deriving (Show)

countOccurrencesOfEachComponent :: Component -> (Int, Int, Int)
countOccurrencesOfEachComponent (TextBox _ _) = (1, 0, 0)
countOccurrencesOfEachComponent (Button _ _) = (0, 1, 0)
countOccurrencesOfEachComponent (Container _ children) = countChildren children
  where
    countChildren :: [Component] -> (Int, Int, Int)
    countChildren [] = (0, 0, 1) -- Add 1 for the container itself
    countChildren (x:xs) =
        let (t1, b1, c1) = countOccurrencesOfEachComponent x
            (t2, b2, c2) = countChildren xs
        in (t1 + t2, b1 + b2, c1 + c2 + 1)


gui :: Component
gui = Container "My App"
        [ Container "Menu"
            [ Button "btn_new" "New"
            , Button "btn_open" "Open"
            , Button "btn_close" "Close"
            ]
        , Container "Body"
            [ TextBox "textbox_1" "Some text goes here" ]
        , Container "Footer" []
        ]



addComponentToContainerAtIndex :: Component -> Component -> String -> Int -> Component
addComponentToContainerAtIndex (Container cname children) newComponent targetName index
    | cname == targetName = Container cname (insertAtIndex children newComponent index)
    | otherwise = Container cname (addToChildren children)
  where
    addToChildren :: [Component] -> [Component]
    addToChildren [] = []
    addToChildren (x:xs) = addComponentToContainerAtIndex x newComponent targetName index : addToChildren xs

    insertAtIndex :: [Component] -> Component -> Int -> [Component]
    insertAtIndex [] comp _ = [comp]
    insertAtIndex (x:xs) comp 0 = comp : x : xs
    insertAtIndex (x:xs) comp n = x : insertAtIndex xs comp (n - 1)
addComponentToContainerAtIndex component _ _ _ = component 
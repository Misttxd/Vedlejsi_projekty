data Component
    = Button String String
    | TextBox String String
    | Container String [Component]
    deriving (Show)

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


countButtons :: Component -> Int
countButtons (Button _ _) = 1
countButtons (TextBox _ _) = 0
countButtons (Container _ children) = countButtonsInList children        
    where   
        countButtonsInList :: [Component] -> Int
        countButtonsInList [] = 0
        countButtonsInList (x:xs) = countButtons x + countButtonsInList xs

addElement :: Component -> Component -> String -> Component
addElement gui newElement tagetName = addToContainer gui
    where
        addToContainer (Container name children)
            | name == tagetName = Container name (children ++ [newElement])
            | otherwise = Container name (map addToContainer children)
        addToContainer other = other

newButton :: Component
newButton = Button "btn_save" "Save"

updatedGui :: Component
updatedGui = addElement gui newButton "Menu"


data Entity
    = Point Double Double                -- Point with two Double coordinates
    | Circle Double Double Int           -- Circle with center (Double, Double) and radius (Int)
    | EntityContainer [Entity]           -- Container with a list of Entity
    deriving (Show)

-- Example instance of the Entity structure
exampleEntity :: Entity
exampleEntity =
    EntityContainer
        [ Point 1.0 2.0                 -- A point at (1.0, 2.0)
        , Circle 0.0 0.0 5             -- A circle centered at (0.0, 0.0) with radius 5
        , EntityContainer
            [ Point 3.0 4.0            -- A nested container with another point
            , Circle 1.0 1.0 10        -- A circle inside the nested container
            ]
        ]


------------------------------------------------------------------------------

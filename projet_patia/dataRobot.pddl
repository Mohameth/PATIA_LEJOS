(define (domain dataRobot)

  (:requirements :strips :typing)

  (:types place physobj - object
          pinceOuverte pinceFerme - pince
          coordX coordY - place
          palet robot - physobj
    )

  (:predicates 	(close  ?obj - pince)
                (open  ?obj - pince)
                (at ?obj - physobj ?loc1 - coordX ?loc2 - coordY)
                (in ?palet - palet ?robot - robot))

  (:action load-palet
    :parameters (?pal - palet ?robot - robot ?coordX - coordX ?coordY - coordY ?modpince - pince)
    :precondition (and (at ?robot ?coordX ?coordY) (and (at ?pal ?coordX ?coordY) (open ?modpince)))
    :effect (and (in ?pal ?robot) (not (open ?modpince)) (close ?modpince) ( not ( at ?pal ?coordX ?coordY )))
  )

  (:action unload-palet
    :parameters (?pal - palet ?robot - robot ?coordX - coordX ?coordY - coordY ?modpince - pince)
    :precondition (and (at ?robot ?coordX ?coordY) (close ?modpince) (in ?pal ?robot))
    :effect (and (not (in ?pal ?robot)) (open ?modpince) (not (close ?modpince)) (at ?pal ?coordX ?coordY))
  )

  (:action goto
    :parameters (?robot - robot ?fromX - coordX ?fromY - coordY ?toX - coordX ?toY - coordY )
    :precondition (at ?robot ?fromX ?fromY )
    :effect (and (not ( at ?robot ?fromX ?fromY)) (at ?robot ?toX ?toY))
  )


)

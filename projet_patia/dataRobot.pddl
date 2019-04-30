(define (domain dataRobot)

  (:requirements :strips :typing)

  (:types place physobj - object
          pinceOuverte pinceFerme - pince
          coord - place
          palet robot - physobj
    )

  (:predicates 	(close  ?obj - pince)
                (open  ?obj - pince)
                (at ?obj - physobj ?loc - coord)
                (isCloser ?pal1 - physobj ?pal2 - physobj)
                (in ?palet - palet ?robot - robot))

  (:action load-palet
    :parameters (?pal - palet ?robot - robot ?coord - coord ?modpince - pince)
    :precondition (and (at ?robot ?coord) (and (at ?pal ?coord) (open ?modpince)))
    :effect (and (in ?pal ?robot) (not (open ?modpince)) (close ?modpince) ( not ( at ?pal ?coord)))
  )

  (:action unload-palet
    :parameters (?pal - palet ?robot - robot ?coord - coord ?modpince - pince)
    :precondition (and (at ?robot ?coord) (close ?modpince) (in ?pal ?robot))
    :effect (and (not (in ?pal ?robot)) (open ?modpince) (not (close ?modpince)) (at ?pal ?coord))
  )

  (:action goto
    :parameters (?robot - robot ?from - coord ?to - coord)
    :precondition (at ?robot ?from)
    :effect (and (not ( at ?robot ?from)) (at ?robot ?to))
  )
)

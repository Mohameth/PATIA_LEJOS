(define (problem pb_robot)
  (:domain dataRobot)

  (:objects
     C3PO - robot
     pal2 pal3 pal1 pal4 - palet
     p - pince
     x10 x8 x9 x7 x6 - coordX
     y100 y9 - coordY)

  (:init (open p)
         (at pal2 x8 y9)
         (at pal1 x9 y9)
         (at pal3 x7 y9)
         (at pal4 x6 y9)

         (at C3PO x10 y100)

  )

  (:goal (and (open p) (at pal1 x10 y100) (at pal2 x10 y100) (at pal3 x10 y100) (at pal4 x10 y100)))

)

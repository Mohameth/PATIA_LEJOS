(define (problem pb_robot)
  (:domain dataRobot)

  (:objects
     C3PO - robot
     pal1 pal2 pal3 - palet
     p - pince
     c10 c8 c5 c11 - coordX
     c5 c9 - coordY)

  (:init (open p)
         (at pal2 c8 c9)
         (at pal3 c8 c9)
         (at C3PO c10 c9)
         (at pal1 c10 c9)
  )

  (:goal (and (in pal1 C3PO) (close p)))

)

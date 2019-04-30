(define (problem pb_robot)
  (:domain dataRobot)

  (:objects
     C3PO - robot
	  p - pince
     pal0 pal1 pal2 - palet
		 emppal0 emppal1 emppal2 emprobot - coord)
 
 (:init (open p)
		(at pal0 emppal0)
		(at pal1 emppal1)
		(at pal2 emppal2)
		( at C3PO emprobot)
  )

  (:goal (and (at pal0 emprobot) (at pal1 emprobot) (at pal2 emprobot) ))
)
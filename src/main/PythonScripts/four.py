from manimlib import *


class FourCircles(Scene):
    def construct(self):
        logo = ImageMobject("enstablanc.png")
        self.add(logo)
        self.play(FadeIn(logo), runtime=1.5)
        #move to the bottom right corner
        self.wait(2)
        self.play(FadeOut(logo))

        circles = []
        triangles = []
        zad = Circle(radius=1, color=GREEN, fill_opacity=0.5,
                stroke_width=0)
        zad.move_to(ORIGIN)
        self.play(FadeIn(zad))
        text = Text("Zone of interest", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        self.play(Flash(zad, color = GREEN, flash_radius = 1.5))
        #move text to the right left corner of the screen
        
        self.wait(3)
        #self.play(FadeOut(zad))
        self.play(FadeOut(text))
        num_circles = 6
        radius = 1
        spacing = 1.5 * radius
        self.wait()
        # create circles and add them to a list
        for i in range(num_circles):
            circle = Circle(radius=0.3, color=BLUE)
            circles.append(circle)
            

        # position circles at the center of the screen
        center = np.array([0, 0, 0])
        for i in range(num_circles):
            circles[i].move_to(center)
            
        text = Text("Establishing a\nmonitoring perimeter", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        # animate circles to spread out
        for i in range(num_circles):
            angle = i / num_circles * TAU
            target_pos = center + 3 * np.array([np.cos(angle), np.sin(angle), 0])
            circles[i].generate_target()
            circles[i].target.move_to(target_pos)
            self.play(ShowCreation(circles[i]), run_time=0.4)
            self.play(MoveToTarget(circles[i]))

            triangle = Polygon(
                
                circles[i].get_center(),
                circles[i].get_center() + 2 * np.array([np.cos(angle - TAU/3), np.sin(angle - TAU/3), 0]),
                circles[i].get_center() + 2 * np.array([np.cos(angle + TAU/3), np.sin(angle + TAU/3), 0]),
                fill_opacity=0.3,
                stroke_width=0
            )
            triangles.append(triangle)
            self.play(ShowCreation(triangle))
            
        # add circles to the scene
        self.add(*circles)
        self.add(*triangles)
        
        self.play(FadeOut(text))
        self.wait()
        text = Text("Defective drone", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        self.play(FocusOn(circles[2], color=RED))
        
        self.play(FadeOut(text))
        self.wait()
        self.play(FadeOut(circles[2]), FadeOut(triangles[2]))
        
        # show arrow
        #compute start and end of arrow. start should be the positoin of circles[2]
        #and end should be the center of the screen (ORIGIN)
        start = circles[2].get_center()
        end = ORIGIN
        arrow_1 = Arrow(start=start, end=end, color=RED)
        text = Text("Breach in the perimeter", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        self.play(ShowCreation(arrow_1))
        #change color of zad to red
        self.play(zad.animate.set_color(RED))
        
        self.play(FadeOut(text))
        self.wait()
        self.play(Uncreate(arrow_1))
        # remove the faded out circles and triangles from the scene
        self.remove(circles[2])
        self.remove(triangles[2])
        #remove the faded out circles and triangles from the lists
        circles.remove(circles[2])
        triangles.remove(triangles[2])
        num_circles -= 1

        self.wait()
        text = Text("Reshaping the\nmonitoring formation", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        #Uncreate all the triangles
        self.play(*[Uncreate(triangle) for triangle in triangles])
        for i in range(num_circles):
            angle = i / num_circles * TAU
            target_pos = center + 3 * np.array([np.cos(angle), np.sin(angle), 0])
            circles[i].generate_target()
            circles[i].target.move_to(target_pos)
            self.play(MoveToTarget(circles[i]))

            triangle = Polygon(
                
                circles[i].get_center(),
                circles[i].get_center() + 2 * np.array([np.cos(angle - TAU/3), np.sin(angle - TAU/3), 0]),
                circles[i].get_center() + 2 * np.array([np.cos(angle + TAU/3), np.sin(angle + TAU/3), 0]),
                fill_opacity=0.3,
                stroke_width=0
            )
            triangles.append(triangle)
            self.play(ShowCreation(triangle))
        self.play(zad.animate.set_color(GREEN))

        self.play(FadeOut(text))
        self.wait()
        #create a new circle
        text = Text("Imposter drone\njoins the swarm", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        circle = Circle(radius=0.3, color=RED)
        circle.move_to(center)
        circles.append(circle)
        num_circles += 1
        self.play(ShowCreation(circle))
        self.play(FadeOut(text))
        self.play(*[Uncreate(triangle) for triangle in triangles])
        text = Text("Reshaping for\noptimization", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        for i in range(num_circles):
            angle = i / num_circles * TAU
            target_pos = center + 3 * np.array([np.cos(angle), np.sin(angle), 0])
            circles[i].generate_target()
            circles[i].target.move_to(target_pos)
            self.play(MoveToTarget(circles[i]))
            
            triangle = Polygon(
                
                circles[i].get_center(),
                circles[i].get_center() + 2 * np.array([np.cos(angle - TAU/3), np.sin(angle - TAU/3), 0]),
                circles[i].get_center() + 2 * np.array([np.cos(angle + TAU/3), np.sin(angle + TAU/3), 0]),
                fill_opacity=0.3,
                stroke_width=0
            )
            triangles.append(triangle)
            self.play(ShowCreation(triangle))
            if i == num_circles - 1:
                self.play(FadeOut(text))
                text = Text("Imposter does not\nmonitor", color=WHITE, font_size=40,font="CMU Serif")
                text.move_to(3.5*LEFT + 3.5*UP)
                self.play(Write(text), runtime=3)
                self.wait(1)
                self.play(Flash(circles[i], color = RED, flash_radius = 0.8))
                self.play(FadeOut(triangle))
                #remove the triangle from the scene
                self.remove(triangle)
                #remove the triangle from the list
                triangles.remove(triangle)

        self.play(FadeOut(text))
        self.wait()
        text = Text("Undetected breach", color=WHITE, font_size=40,font="CMU Serif")
        text.move_to(3.5*LEFT + 3.5*UP)
        self.play(Write(text), runtime=3)
        self.wait(1)
        #put red arrow at the last circle pointing to the center
        start = circles[num_circles - 1].get_center()
        end = ORIGIN
        arrow_2 = Arrow(start=start, end=end, color=RED)
        arrow_3 = Arrow(start=start, end=end, color=RED)
        #shift the arrows a bit
        arrow_2.shift(0.35*RIGHT)
        arrow_2.shift(0.25*UP)
        arrow_3.shift(0.35*LEFT)
        arrow_3.shift(0.35*DOWN)
        self.play(ShowCreation(arrow_2), ShowCreation(arrow_3))
        self.wait()
        #zad turns red
        self.play(zad.animate.set_color(RED))

        self.play(FadeOut(arrow_2), FadeOut(arrow_3))
        self.play(FadeOut(text))
        #uncreate all the triangles

        self.play(*[Uncreate(triangle) for triangle in triangles])
        self.wait()
        #uncreate all the circles
        self.play(*[Uncreate(circle) for circle in circles])
        #fadeout zad
        self.play(FadeOut(zad))
        self.wait()
       
       
        
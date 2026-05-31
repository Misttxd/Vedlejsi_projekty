import { type CSSProperties, useEffect, useState } from 'react';
import { motion, type Variants } from 'framer-motion';
import Tilt from 'react-parallax-tilt';
import { Typewriter } from 'react-simple-typewriter';
import { GitBranch as GithubIcon, Mail, Code2, Cpu, Database, Layout, Terminal, Braces, GraduationCap, ChevronRight, ExternalLink } from 'lucide-react';
import './App.css';

type CSSCustomProperties = CSSProperties & Record<`--${string}`, string>;

const fadeIn: Variants = {
  hidden: { opacity: 0, y: 30 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.6, ease: "easeOut" } }
};

const staggerContainer: Variants = {
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: 0.15
    }
  }
};

function App() {
  const [scrolled, setScrolled] = useState(false);
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 50);
    };
    const handleMouseMove = (e: MouseEvent) => {
      setMousePosition({ x: e.clientX, y: e.clientY });
    };

    window.addEventListener('scroll', handleScroll);
    window.addEventListener('mousemove', handleMouseMove);
    return () => {
      window.removeEventListener('scroll', handleScroll);
      window.removeEventListener('mousemove', handleMouseMove);
    };
  }, []);

  const technologies = [
    { name: 'TypeScript', icon: <Braces size={32} />, color: '#3178c6' },
    { name: 'React & Next.js', icon: <Layout size={32} />, color: '#61dafb' },
    { name: 'C/C++', icon: <Cpu size={32} />, color: '#00599C' },
    { name: 'C# & Unity', icon: <Code2 size={32} />, color: '#9B4F96' },
    { name: 'Java', icon: <Terminal size={32} />, color: '#b07219' },
    { name: 'Haskell & Assembly', icon: <Database size={32} />, color: '#5e5086' },
  ];

  const projects = [
    {
      title: 'Day-planner',
      description: 'A robust task planner built on a modern web stack for efficient daily organization. Utilizes Next.js App Router and server actions for seamless data mutations.',
      tech: ['TypeScript', 'Next.js', 'React', 'Tailwind CSS'],
      github: 'https://github.com/Misttxd/Day-planner',
      demo: '#',
      image: 'https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Object Storage API',
      description: 'A comprehensive backend API for object storage. Features include bucket management, user billing, soft delete mechanisms, and strict data validation.',
      tech: ['Python', 'FastAPI', 'SQLAlchemy', 'Pydantic'],
      github: 'https://github.com/Misttxd/Vedlejsi_projekty',
      demo: '#',
      image: 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Maturita Game',
      description: 'An interactive fishing video game featuring custom-built mechanics, physics systems, and complex AI behaviors for various fish species.',
      tech: ['C#', 'Unity', 'Blender'],
      github: 'https://github.com/Misttxd/Maturita',
      demo: '#',
      image: 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=800&q=80'
    },
    {
      title: 'Java Rhythm Game',
      description: 'A desktop rhythm application where players hit falling notes with high accuracy. Implements a custom scoring algorithm and H2 database for data persistence.',
      tech: ['Java', 'JavaFX', 'Maven', 'H2'],
      github: 'https://github.com/Misttxd/Vedlejsi_projekty',
      demo: '#',
      image: 'https://images.unsplash.com/photo-1511512578047-dfb367046420?auto=format&fit=crop&w=800&q=80'
    }
  ];

  const experience = [
    {
      title: 'Computer Science Student',
      organization: 'VŠB - Technical University of Ostrava',
      period: 'Present',
      description: 'Focusing on software engineering, low-level programming, and algorithmic problem solving. Developing complex backend architectures and scalable frontend applications.',
      icon: <GraduationCap size={20} />
    }
  ];

  return (
    <div className="app-container" style={{ '--mouse-x': `${mousePosition.x}px`, '--mouse-y': `${mousePosition.y}px` } as CSSCustomProperties}>
      <div className="spotlight"></div>
      <div className="background-effects">
        <div className="glow-orb orb-1"></div>
        <div className="glow-orb orb-2"></div>
      </div>

      <nav className={`navbar ${scrolled ? 'scrolled' : ''}`}>
        <div className="nav-logo">A.B.</div>
        <div className="nav-links">
          <a href="#about">Profile</a>
          <a href="#expertise">Expertise</a>
          <a href="#projects">Work</a>
          <a href="#contact">Contact</a>
        </div>
      </nav>

      <main>
        <section id="about" className="hero section-divider">
          <div className="hero-graphic">
            <div className="orbital-stage" aria-label="Portfolio navigation orbit">
              <svg className="orbit-map" viewBox="0 0 650 650" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <circle cx="325" cy="325" r="300" className="orbit-ring orbit-ring-outer" />
                <circle cx="325" cy="325" r="200" className="orbit-ring" />
                <circle cx="325" cy="325" r="100" className="orbit-ring orbit-ring-inner" />
                <path d="M325 25L325 625" className="orbit-axis" />
                <path d="M25 325L625 325" className="orbit-axis" />
                <rect x="225" y="225" width="200" height="200" className="orbit-diamond" transform="rotate(45 325 325)" />
                <rect x="255" y="255" width="140" height="140" className="orbit-diamond orbit-diamond-soft" transform="rotate(22.5 325 325)" />
              </svg>

              <div className="orbit-track">
                <a href="#projects" className="orbit-node orbit-node-top">
                  <span className="node-shell">
                    <span className="node-pulse dot-shade-1"></span>
                    <span className="node-label">WORK</span>
                  </span>
                </a>
                <a href="#expertise" className="orbit-node orbit-node-right">
                  <span className="node-shell">
                    <span className="node-pulse dot-shade-2"></span>
                    <span className="node-label">EXPERTISE</span>
                  </span>
                </a>
                <a href="#contact" className="orbit-node orbit-node-bottom">
                  <span className="node-shell">
                    <span className="node-pulse dot-shade-3"></span>
                    <span className="node-label">CONTACT</span>
                  </span>
                </a>
                <a href="#about" className="orbit-node orbit-node-left">
                  <span className="node-shell">
                    <span className="node-pulse dot-shade-4"></span>
                    <span className="node-label">PROFILE</span>
                  </span>
                </a>
              </div>
            </div>
          </div>
          <motion.div 
            initial="hidden"
            animate="visible"
            variants={staggerContainer}
            className="hero-content"
          >
            <motion.div variants={fadeIn} className="hero-badge">
              <span className="badge-dot"></span> Available for new opportunities
            </motion.div>
            <motion.h1 variants={fadeIn} className="hero-title">
              Andreas <span className="hero-title-accent">Brudovský</span>
            </motion.h1>
            <motion.h2 variants={fadeIn} className="hero-profession">
              <span style={{ color: 'var(--text-secondary)' }}>&gt; </span>
              <Typewriter
                words={['Software Engineer', 'Computer Science Student', 'Backend Architect', 'React Developer', 'C/C++ Enthusiast']}
                loop={0}
                cursor
                cursorStyle='_'
                typeSpeed={70}
                deleteSpeed={50}
                delaySpeed={2000}
              />
            </motion.h2>
            <motion.p variants={fadeIn} className="hero-subtitle">
              Designing robust backend architectures, interactive applications, and scalable web solutions. Specialized in strongly-typed ecosystems and algorithmic optimization.
            </motion.p>
            <motion.div variants={fadeIn} className="hero-actions">
              <a href="#projects" className="btn btn-primary">
                Explore Work <ChevronRight size={18} />
              </a>
              <a href="https://github.com/misttxd" target="_blank" rel="noopener noreferrer" className="btn btn-secondary">
                <GithubIcon size={20} /> GitHub Profile
              </a>
            </motion.div>
          </motion.div>
        </section>

        <section id="expertise" className="section section-divider">
          <motion.div 
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: "-100px" }}
            variants={staggerContainer}
          >
            <div className="section-header">
              <motion.h2 variants={fadeIn} className="section-title">Technical Expertise</motion.h2>
              <motion.p variants={fadeIn} className="section-description">
                A carefully curated stack of technologies I use to build robust software systems. Hover over them to see the spark.
              </motion.p>
            </div>
            
            <motion.div className="tech-grid" variants={staggerContainer}>
              {technologies.map((tech, index) => (
                <motion.div key={index} variants={fadeIn}>
                  <Tilt 
                    tiltMaxAngleX={15} 
                    tiltMaxAngleY={15} 
                    scale={1.05} 
                    transitionSpeed={2000}
                    className="tilt-wrapper"
                  >
                    <div className="tech-item" style={{ '--hover-color': tech.color } as CSSCustomProperties}>
                      <div className="tech-icon-wrapper">
                        {tech.icon}
                      </div>
                      <span className="tech-name">{tech.name}</span>
                    </div>
                  </Tilt>
                </motion.div>
              ))}
            </motion.div>
          </motion.div>
        </section>

        <section className="section section-divider">
          <motion.div
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: "-100px" }}
            variants={staggerContainer}
          >
            <div className="section-header">
              <motion.h2 variants={fadeIn} className="section-title">Journey</motion.h2>
            </div>

            <div className="timeline">
              {experience.map((item, index) => (
                <motion.div key={index} variants={fadeIn} className="timeline-item">
                  <Tilt tiltMaxAngleX={5} tiltMaxAngleY={5} scale={1.01} className="timeline-content-wrapper">
                    <div className="timeline-icon">{item.icon}</div>
                    <div className="timeline-content">
                      <span className="timeline-period">{item.period}</span>
                      <h3 className="timeline-title">{item.title}</h3>
                      <h4 className="timeline-org">{item.organization}</h4>
                      <p className="timeline-desc">{item.description}</p>
                    </div>
                  </Tilt>
                </motion.div>
              ))}
            </div>
          </motion.div>
        </section>

        <section id="projects" className="section section-divider">
          <motion.div
            initial="hidden"
            whileInView="visible"
            viewport={{ once: true, margin: "-100px" }}
            variants={staggerContainer}
          >
            <div className="section-header">
              <motion.h2 variants={fadeIn} className="section-title">Selected Work</motion.h2>
              <motion.p variants={fadeIn} className="section-description">
                A showcase of my recent architectural implementations and engineering projects.
              </motion.p>
            </div>

            <motion.div className="bento-grid-projects" variants={staggerContainer}>
              {projects.map((project, index) => (
                <motion.div key={index} variants={fadeIn} className={`bento-item bento-project-${index}`}>
                  <Tilt 
                    tiltMaxAngleX={5} 
                    tiltMaxAngleY={5} 
                    glareEnable={true} 
                    glareMaxOpacity={0.1} 
                    glareColor="#ffffff" 
                    glarePosition="all"
                    transitionSpeed={1500}
                    className="project-card-wrapper"
                  >
                    <div className="glass-card project-card group">
                      <div className="project-content">
                        <div className="project-header">
                          <h3 className="project-title">{project.title}</h3>
                          <div className="project-links">
                            <a href={project.github} target="_blank" rel="noopener noreferrer" aria-label="GitHub Repository">
                              <GithubIcon size={20} />
                            </a>
                            {project.demo !== '#' && (
                              <a href={project.demo} target="_blank" rel="noopener noreferrer" aria-label="Live Demo">
                                <ExternalLink size={20} />
                              </a>
                            )}
                          </div>
                        </div>
                        <p className="project-desc">{project.description}</p>
                      </div>
                      <div className="project-footer">
                        <div className="project-tech">
                          {project.tech.map((t, i) => (
                            <span key={i} className="tech-tag">{t}</span>
                          ))}
                        </div>
                      </div>
                    </div>
                  </Tilt>
                </motion.div>
              ))}
            </motion.div>
          </motion.div>
        </section>

        <section id="contact" className="section contact-section">
          <motion.div 
            initial={{ opacity: 0, y: 40 }}
            whileInView={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8, ease: "easeOut" }}
            viewport={{ once: true }}
            className="contact-card-wrapper"
          >
            <Tilt tiltMaxAngleX={5} tiltMaxAngleY={5} glareEnable={true} glareMaxOpacity={0.05} scale={1.02} transitionSpeed={2000}>
              <div className="contact-card">
                <h2>Initiate a Conversation</h2>
                <p>
                  I am currently open to new engineering opportunities and challenging projects. 
                  Whether you have a specific proposal or just want to connect, my inbox is open.
                </p>
                <a href="mailto:andreas.brudovsky@example.com" className="btn btn-primary btn-large">
                  <Mail size={20} /> Contact Me
                </a>
              </div>
            </Tilt>
          </motion.div>
        </section>
      </main>

      <footer className="footer">
        <div className="footer-content">
          <div className="nav-logo">A.B.</div>
          <p>© {new Date().getFullYear()} Andreas Brudovský. Engineered with React.</p>
        </div>
      </footer>
    </div>
  );
}

export default App;

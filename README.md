# rebuild_syncup

GOAL:
  -Make coordinating events with groups of people more efficient.

CONCEPT:
  -Each user gathers 'friends'.  Friends are invited to 'events'  Each member of an event declares a few availabilities.  Availabilities are visualized for easy scheduling.  Eventually, a final date is decided upon and marked.
  -Early concept image http://imgur.com/1nJaRLW
  
TECHNICAL ROADMAP:
  -The user inputs availability information with DatePickers and TimePickers.  This information is added to an ArrayList<HashMap>, and stored in an XML.  It is then sent to all members of that event and stored in local XML's as a distributed database.
  -All availabilities are overlaid over a grid representing a 1 week span (see concept image) for a graphical representation of when the event can take place.  Personal availabilities are also shown in views below, where they can be deleted.
  
POTENTIAL PROBLEMS:
  -Time inputs can be cumbersome, and people are lazy.  Entering information into the app should be as streamlined as is possible.  This will wait until after the minimum viable product is out.
  -Visual design is bland, could use work.  Clarity remains a higher priority, however.
  -This may not add enough utility over group texts for users to make use of it.
  -I'm learning Java as I go, which is not easy.  I currently have an overreliance on static variables that really needs to be reigned in.

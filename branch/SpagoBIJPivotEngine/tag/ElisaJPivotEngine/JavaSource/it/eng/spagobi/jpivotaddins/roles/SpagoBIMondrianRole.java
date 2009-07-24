/*
// $Id: //open/mondrian-release/3.0/src/main/mondrian/olap/RoleImpl.java#3 $
// This software is subject to the terms of the Common Public License
// Agreement, available at the following URL:
// http://www.opensource.org/licenses/cpl.html.
// Copyright (C) 2002-2002 Kana Software, Inc.
// Copyright (C) 2002-2007 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, Oct 5, 2002
*/

package it.eng.spagobi.jpivotaddins.roles;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;

import java.util.*;
import mondrian.olap.*;

/**
 * <code>RoleImpl</code> is Mondrian's default implementation for the
 * <code>Role</code> interface.
 *
 * @author jhyde
 * @since Oct 5, 2002
 * @version $Id: //open/mondrian-release/3.0/src/main/mondrian/olap/RoleImpl.java#3 $
 */
public class SpagoBIMondrianRole implements Role {

	private IEngUserProfile profile = null;
    /**
     * Creates a role with no permissions.
     * @param profile 
     */
    public SpagoBIMondrianRole(IEngUserProfile profile) {
    	this.profile = profile;
    }

    public Access getAccess(Schema schema) {
        return Access.ALL;
    }

    public Access getAccess(Cube cube) {
    	if (cube.getName().equalsIgnoreCase("Products")) {
    		return Access.CUSTOM;
    	}
        return Access.ALL;
    }

    public Access getAccess(Dimension dimension) {
    	// Never invoked
    	if (dimension.getName().equalsIgnoreCase("Customers")) {
    		return Access.NONE;
    	}
    	if (dimension.getName().equalsIgnoreCase("Product")) {
    		return Access.CUSTOM;
    	}
        return Access.ALL;
    }

    public Access getAccess(Hierarchy hierarchy) {
    	if (hierarchy.getName().equalsIgnoreCase("Product")) {
    		return Access.CUSTOM;
    	}
    	return Access.ALL;
    }

    public HierarchyAccess getAccessDetails(Hierarchy hierarchy) {
        return new SpagoBIHierarchyAccess(hierarchy);
    }

    public Access getAccess(Level level) {
    	SpagoBIHierarchyAccess h = new SpagoBIHierarchyAccess(level.getHierarchy());
    	return h.getAccess(level);
    }

    public Access getAccess(Member member) {
    	SpagoBIHierarchyAccess h = new SpagoBIHierarchyAccess(member.getHierarchy());
    	return h.getAccess(member);
    }

    public Access getAccess(NamedSet set) {
        Util.assertPrecondition(set != null, "set != null");
        return Access.ALL;
    }

    public boolean canAccess(OlapElement olapElement) {
        Util.assertPrecondition(olapElement != null, "olapElement != null");
        if (olapElement instanceof Member) {
            return getAccess((Member) olapElement) != Access.NONE;
        } else if (olapElement instanceof Level) {
            return getAccess((Level) olapElement) != Access.NONE;
        } else if (olapElement instanceof NamedSet) {
            return getAccess((NamedSet) olapElement) != Access.NONE;
        } else if (olapElement instanceof Hierarchy) {
            return getAccess((Hierarchy) olapElement) != Access.NONE;
        } else if (olapElement instanceof Cube) {
            return getAccess((Cube) olapElement) != Access.NONE;
        } else if (olapElement instanceof Dimension) {
            return getAccess((Dimension) olapElement) != Access.NONE;
        } else {
            return false;
        }
    }

    public class SpagoBIHierarchyAccess implements HierarchyAccess {

	    private final Map<Member, Access> memberGrants =
	        new HashMap<Member, Access>();
		
		protected Hierarchy hierarchy;
		
		protected Access access = null;
		
		public SpagoBIHierarchyAccess (Hierarchy hierarchy) {
			this.hierarchy = hierarchy;
			if (hierarchy.getName().equals("Product")) {
	        	this.access = Access.CUSTOM;
			} else {
				this.access = Access.ALL;
			}
		}
		
	    public Access getAccess(Level level) {
	        if (this.access != Access.CUSTOM) {
	            return this.access;
	        }
	        if (level.getDepth() < getTopLevelDepth()) {
	            // no access
	            return Access.NONE;
	        } else if (level.getDepth() > getBottomLevelDepth()) {
	            // no access
	            return Access.NONE;
	        }
	        return this.access;
	    }
	    
	    public Access getAccess(Member member) {
	        if (this.access != Access.CUSTOM) {
	            return this.access;
	        }
	        return Access.ALL;
	        
//	        try {
//	        	Access toReturn = Access.NONE;
//	        	// if level is higher than top level or lower than bottom level, no access
//	        	if (member.getLevel().getDepth() < getTopLevelDepth()) {
//		            // no access
//	        		toReturn = Access.NONE;
//		        } else if (member.getLevel().getDepth() > getBottomLevelDepth()) {
//		            // no access
//		        	toReturn = Access.NONE;
//		        } else {
//		        	if (member.isAll()) {
//		        		toReturn = Access.ALL;
//		        	} else {
//			        	String uniqueName = member.getUniqueName();
//						if (uniqueName.startsWith("[Product].[All Products].[" + profile.getUserAttribute("family").toString() + "]")) {
//							toReturn = Access.ALL;
//						} else {
//							toReturn = Access.NONE;
//						}
//		        	}
//		        }
//	        	
//				System.out.println("getAccess: returning " + toReturn + " for member " + member);
//				return toReturn;
//			} catch (EMFInternalError e) {
//				e.printStackTrace();
//				return Access.NONE;
//			}

	    }
		
//	    public Access getAccess(Member member) {
//	        if (this.access != Access.CUSTOM) {
//	            return this.access;
//	        }
//	        try {
//	        	String uniqueName = member.getUniqueName();
//				if (uniqueName.equals("[Product].[" + profile.getUserAttribute("family").toString() + "]")) {
//					if (!memberGrants.containsKey(member)) {
//						memberGrants.put(member, Access.ALL);
//					}
//				}
//			} catch (EMFInternalError e) {
//				e.printStackTrace();
//			}
//	        /*
//	        if (member.getUniqueName().equals("[Product].[All Products].[Drink]")) {
//	        	if (!memberGrants.containsKey(member)) {
//	        		memberGrants.put(member, Access.NONE);
//	        	}
//	        }
//	        if (member.getUniqueName().equals("[Product].[All Products].[Food]")) {
//	        	if (!memberGrants.containsKey(member)) {
//	        		memberGrants.put(member, Access.ALL);
//	        	}
//	        }
//	        */
//	        if (member.getLevel().getDepth() < getTopLevelDepth()) {
//	            // no access
//	            return Access.NONE;
//	        } else if (member.getLevel().getDepth() > getBottomLevelDepth()) {
//	            // no access
//	            return Access.NONE;
//	        } else {
//	            // Check whether there is an explicit grant for the member or
//	            // an ancestor.
//	            for (Member m = member; m != null; m = m.getParentMember()) {
//	                final Access memberAccess = memberGrants.get(m);
//	                if (memberAccess == null) {
//	                    continue;
//	                }
//	                if (memberAccess == Access.CUSTOM &&
//	                        m != member) {
//	                    // If member's ancestor has custom access, that
//	                    // means that member has no access.
//	                    return Access.NONE;
//	                }
//	                return memberAccess;
//	            }
//	            // If there is no inherited access, check for implicit access.
//	            // A member is implicitly visible if one of its descendants is
//	            // visible.
//	            for (Map.Entry<Member, Access> entry : memberGrants.entrySet()) {
//	                final Member grantedMember = entry.getKey();
//	                switch (entry.getValue()) {
//	                case NONE:
//	                    continue;
//	                }
//	                for (Member m = grantedMember; m != null; m = m.getParentMember()) {
//	                    if (m == member) {
//	                        return Access.CUSTOM;
//	                    }
//	                    if (m != grantedMember && memberGrants.get(m) != null) {
//	                        break;
//	                    }
//	                }
//	            }
//	            return Access.NONE;
//	        }
//	    	/*
//	    	if (member.getUniqueName().equals("[Product].[All Products]")) return Access.CUSTOM;
//	    	if (member.getUniqueName().equals("[Product].[All Products].[Drink]")) return Access.NONE;
//	    	if (member.getUniqueName().equals("[Product].[All Products].[Food]")) return Access.ALL;
//	//    	Level level = member.getLevel();
//	//    	int depth = level.getDepth();
//	//    	if (depth < this.getTopLevelDepth() || depth > this.getBottomLevelDepth()) {
//	//    		return Access.NONE;
//	//    	}
//	        return Access.ALL;
//	        */
//	    }
	
	    public int getTopLevelDepth() {
	    	if (hierarchy.getName().equals("Product")) {
	    		return 0;
	    	}
	        return 0;
	    }
	
	    public int getBottomLevelDepth() {
//	    	if (hierarchy.getName().equals("Product")) {
//	    		return 2;
//	    	}
	        return hierarchy.getLevels().length - 1;
	    }
	
	    public RollupPolicy getRollupPolicy() {
	    	if (hierarchy.getName().equals("Product")) {
	    		return RollupPolicy.PARTIAL;
	    	}
	    	return RollupPolicy.FULL;
	    }
	
	    public boolean hasInaccessibleDescendants(Member member) {
	    	boolean toReturn = false;
	    	if (member == null) toReturn = false;
	    	else {
		    	if (member.isAll()) {
		    		toReturn = true;
	        	} else {
	        		toReturn = false;
	        	}
	    	}
			System.out.println("hasInaccessibleDescendants: returning " + toReturn + " for member " + member);
        	return toReturn;
//        	try {
//        		boolean toReturn = false;
//        		if (member == null) toReturn = false;
//        		else {
//			    	// if level is higher than top level or lower than bottom level, no access
//		        	if (member.getLevel().getDepth() < getTopLevelDepth()) {
//		        		toReturn = true;
//			        } else if (member.getLevel().getDepth() > getBottomLevelDepth()) {
//			        	toReturn = false;
//			        } else {
//			        	if (member.isAll()) {
//			        		toReturn = true;
//			        	} else {
//				        	String uniqueName = member.getUniqueName();
//							if (uniqueName.startsWith("[Product].[All Products].[" + profile.getUserAttribute("family").toString() + "]")) {
//								toReturn = false;
//							} else {
//								toReturn = true;
//							}
//			        	}
//			        }
//        		}
//				System.out.println("hasInaccessibleDescendants: returning " + toReturn + " for member " + member);
//	        	return toReturn;
//			} catch (EMFInternalError e) {
//				e.printStackTrace();
//				return true;
//			}
	    }
	    
	}


}

// End RoleImpl.java

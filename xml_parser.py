#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon May  4 23:18:35 2020

@author: harman
"""

from lxml import etree
from itertools import combinations
import csv



path = '/users/harman/desktop/illinois-latest.osm'


node_csv_file = '/users/harman/desktop/node.csv'
way_csv_file = '/users/harman/desktop/way.csv'



possible_types = set()
possible_types.add('highway####motorway')
possible_types.add('highway####trunk')
possible_types.add('highway####primary')
possible_types.add('highway####secondary')
possible_types.add('highway####tertiary')
possible_types.add('highway####unclassified')
possible_types.add('highway####residential')
possible_types.add('highway####living_street')
possible_types.add('highway####unclassified')
possible_types.add('highway####service')
possible_types.add('highway####motorway_link')
possible_types.add('highway####trunk_link')
possible_types.add('highway####primary_link')
possible_types.add('highway####secondary_link')
possible_types.add('highway####tertiary_link')



num_ways = 0


context = etree.iterparse(path, events=('end',), tag=('node','way'))
node_info = [('id','lat','lng')]
ways_info = [('id', 'type', 'starting', 'ending')]
for event, elem in context:
    possible_nodes = []
    current_type = None
    
    if elem.tag=='way':
        
        for c in elem:
            if c.tag=='nd':
                possible_nodes.append(c.attrib['ref'])
            elif c.tag=='tag':
                #print(c.attrib['k']+'####'+c.attrib['v'])
                if c.attrib['k']+'####'+c.attrib['v'] in possible_types:
                    current_type = c.attrib['v']
                    
        if current_type is not None:
            
            for node_pair in list(combinations(possible_nodes,2)):
                ways_info.append((node_pair[0]+node_pair[1], current_type, node_pair[0], node_pair[1]))
        continue
    
    
# =============================================================================
#     
#     Id=elem.attrib['id']	
#     lat=str(elem.attrib['lat'])
#     lng=str(elem.attrib['lon'])
#     node_info.append((Id,lat,lng))
# =============================================================================
        
elem.clear()
while elem.getprevious() is not None:
    del elem.getparent()[0]

# =============================================================================
# 
# with open(node_csv_file, 'w') as f:
#     writer = csv.writer(f)
#     writer.writerows(node_info)
# =============================================================================
   
   
with open(way_csv_file, 'w') as g:
    writer = csv.writer(g)
    writer.writerows(ways_info)

    

    
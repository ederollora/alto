# ALTO service for the ONOS SDN controller

This is the developed service for the ONOS SDN controller that includes the ALTO protocol. 

It also included some side developments to meet the Master thesis use case. Part of the app accounted for the unused bandwidth towards several CDNs. This information is mixed with the ALTO criteria (e.g., hop count) to provide a proper criteria to forward traffic to a proper CDN

It was developed for ONOS 1.7 so it should probably be modified to meet latest ONOS APIs.


## Citation

If you plan to use, modify, develop, publish or use it for commercial purposes please make sure you mention,a t least the Technical University of Denmark and my name. 

One might also use:

```bibtex
@mastersthesis{onos_alto,
    author = {{Ollora Zaballa, E.}},
    institution = {Master thesis - Technical University of Denmark},
    pages = 90,
    school = {Master thesis - DTU Fotonik},
    title = {{SDN Network integration with ALTO protocol services}},
    year = 2017
}
```

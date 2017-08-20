package com.choranet.commun


class HorlogeJob {
    
    def choraBarrageService
    
    static triggers = {
        simple name: 'horlogeTrigger', startDelay: 300000, repeatInterval: 7200000  
    }
    def group = "SecurityGroup"

    def execute(){
        choraBarrageService.actualiserDernierAccess()
    }
}

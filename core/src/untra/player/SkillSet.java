package untra.player;

import java.util.ArrayList;
import java.util.Iterator;

import untra.database.ActiveSkill;
import untra.database.PassiveSkill;
import untra.database.ReactiveSkill;
import untra.database.Skill;
import untra.database.Skill_Scope;
import untra.database.Weapon_Type;

public class SkillSet {
	private int[] _counter = new int[4];
	private int _count = 0;
	private Actor _actor;
	//private ArrayList<Skill> _staticskills;
	private ArrayList<ActiveSkill> _activeskills;
	private ArrayList<PassiveSkill> _passiveskills;
	private ArrayList<ReactiveSkill> _reactiveskills;
	private ArrayList<Skill> _skills;
	
	public class ActiveSkills implements Iterable<ActiveSkill> {
		  @Override
		  public Iterator<ActiveSkill> iterator() {
		   return _activeskills.iterator();
		  }
		 }
	
	public class PassiveSkills implements Iterable<PassiveSkill> {
		  @Override
		  public Iterator<PassiveSkill> iterator() {
		   return _passiveskills.iterator();
		  }
		 }
	
	public class ReactiveSkills implements Iterable<ReactiveSkill> {
		  @Override
		  public Iterator<ReactiveSkill> iterator() {
		   return _reactiveskills.iterator();
		  }
		 }
	
	public class Skills implements Iterable<Skill> {
		  @Override
		  public Iterator<Skill> iterator() {
			  _skills = new ArrayList<Skill>();
			  //_skills.addAll(_staticskills);
			  _skills.addAll(_activeskills);
			  _skills.addAll(_reactiveskills);
			  _skills.addAll(_passiveskills);
		   return _skills.iterator();
		  }
		 }
	
	public SkillSet(Actor A)
	{
		_actor = A;
		//initialize our arrays
		_activeskills = new ArrayList<ActiveSkill>();
		_passiveskills = new ArrayList<PassiveSkill>();
		_reactiveskills = new ArrayList<ReactiveSkill>();
	}
	
	public void add(Skill s)
	{
		s.acquire();
		_count++;
		if(s instanceof ActiveSkill)
			if(!_activeskills.contains(s));
		{
				_activeskills.add((ActiveSkill) s);
				_counter[1]++;
		}
		if(s instanceof PassiveSkill)
			if(!_passiveskills.contains(s));
		{
				_passiveskills.add((PassiveSkill) s);
				_counter[2]++;
		}
		if(s instanceof ReactiveSkill)
			if(!_reactiveskills.contains(s));
		{
				_reactiveskills.add((ReactiveSkill) s);
				_counter[3]++;
		}
	}
	
	public ActiveSkills activeskills()
	{
		return new ActiveSkills();
	}
	
	public PassiveSkills passiveskills()
	{
		return new PassiveSkills();
	}
	
	public ReactiveSkills reactiveskills()
	{
		return new ReactiveSkills();
	}
	
	public Skills skills()
	{
		return new Skills();
	}
	
	/**
	 * returns the size of all total skills.
	 * @return
	 */
	public int size()
	{
		return _count;
	}
	
	public int size(Skill_Scope s)
	{
		switch (s) {
		case active:
			return _counter[1];
		case passive:
			return _counter[2];
		case reactive:
			return _counter[3];
		default:
			return size();
		}
	}
	
	public Skill get(Skill_Scope s, int i)
	{
		switch (s) {
		case active:
			return _activeskills.get(i);
		case passive:
			return _passiveskills.get(i);
		case reactive:
			return _reactiveskills.get(i);
		default:
			return null;
		}
	}
	
	
	public boolean can_wield(Weapon_Type t)
	{
		int i = t.ordinal();
		i += PassiveSkill.P_START_INDEX;
		for(PassiveSkill p : passiveskills())
		{
			if(p.id == i)
				return true;
		}
		return false;
	}
	
	//
	//
	
	/**
	 * returns true if a reaction triggers
	 * event 0 : on critical damage
	 * event 1 : on hit
	 * event 2 : on target
	 * @return
	 */
	public boolean reactive_trigger(int event)
	{
		for(ReactiveSkill r : reactiveskills())
		{
			//on critical damage
			if(event == 0)
			{
				if(r.crit_trigger())
					return true;
			}
			//on hit
			else if(event == 1)
			{
				if(r.hit_trigger())
					return true;
			}
			//on targetted
			else if(event == 2)
			{
				if(r.targetted_trigger())
					return true;
			}	
		}
		return false;
	}
	
	
	
	
}
